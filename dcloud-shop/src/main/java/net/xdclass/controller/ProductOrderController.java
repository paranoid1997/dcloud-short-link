package net.xdclass.controller;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.annotation.RepeatSubmit;
import net.xdclass.constant.RedisKey;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.controller.request.ProductOrderPageRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.ClientTypeEnum;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.service.ProductOrderService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */


@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {


    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;



    /**
     * 下单前获取令牌用于防重提交
     * @return
     */
    @GetMapping("token")
    public JsonData getOrderToken(){
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String token = CommonUtil.getStringNumRandom(32);
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY,accountNo,token);
        //令牌有效时间是30分钟
        redisTemplate.opsForValue()
                .set(key, String.valueOf(Thread.currentThread().getId()),30, TimeUnit.MINUTES);
        return JsonData.buildSuccess(token);
    }



    /**
     * 分页接口
     * @return
     */
    @PostMapping("page")
    public JsonData page(
            @RequestBody ProductOrderPageRequest orderPageRequest
    ) {
        Map<String, Object> pageResult = productOrderService.page(orderPageRequest);
        return JsonData.buildSuccess(pageResult);
    }


    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("query_state")
    public JsonData queryState(@RequestParam(value = "out_trade_no") String outTradeNo) {
        String state = productOrderService.queryProductOrderState(outTradeNo);
        return StringUtils.isBlank(state) ?
                JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST) : JsonData.buildSuccess(state);
    }


    /**
     * 下单接口
     * @param orderRequest
     * @param response
     */
    @PostMapping("confirm")
//    @RepeatSubmit(limitType = RepeatSubmit.Type.PARAM)
    public void confirmOrder(@RequestBody ConfirmOrderRequest orderRequest, HttpServletResponse response) {
        JsonData jsonData = productOrderService.confirmOrder(orderRequest);
        if (jsonData.getCode() == 0) {
            //支付成功
            //端类型
            String client = orderRequest.getClientType();
            //支付类型
            String payType = orderRequest.getPayType();
            //如果是支付宝支付，跳转网页，sdk除外
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
                if (client.equalsIgnoreCase(ClientTypeEnum.PC.name())) {
                    CommonUtil.sendHtmlMessage(response, jsonData);
                } else if (client.equalsIgnoreCase(ClientTypeEnum.APP.name())) {

                } else if (client.equalsIgnoreCase(ClientTypeEnum.H5.name())) {

                }
            } else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_PAY.name())) {
                //微信支付
                CommonUtil.sendJsonMessage(response, jsonData);
            }
        } else {
            //支付失败
            log.error("创建订单失败{}", jsonData.toString());
            CommonUtil.sendJsonMessage(response, jsonData);
        }
    }
}