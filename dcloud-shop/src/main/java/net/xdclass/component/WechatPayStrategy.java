package net.xdclass.component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.WechatPayApi;
import net.xdclass.config.WechatPayConfig;
import net.xdclass.vo.PayInfoVO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 刘森飚
 * @since 2023-02-05
 * 微信支付
 */

@Service
@Slf4j
public class WechatPayStrategy  implements  PayStrategy{


    @Autowired
    private WechatPayConfig payConfig;

    @Autowired
    private CloseableHttpClient wechatPayClient;


    /**
     * 统一下单
     * @param payInfoVO
     * @return
     */
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        //过期时间  RFC 3339格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        //支付订单过期时间
        String timeExpire = sdf.format(new Date(System.currentTimeMillis() + payInfoVO.getOrderPayTimeoutMills()));
        JSONObject amountObj = new JSONObject();
        //数据库存储是double比如，100.99元，微信支付需要以分为单位
        int amount = payInfoVO.getPayFee().multiply(BigDecimal.valueOf(10)).intValue();
        amountObj.put("total", amount);
        amountObj.put("currency", "CNY");
        JSONObject payObj = new JSONObject();
        payObj.put("mchid", payConfig.getMchId());
        payObj.put("out_trade_no", payInfoVO.getOutTradeNo());
        payObj.put("appid", payConfig.getWxPayAppid());
        payObj.put("description", payInfoVO.getTitle());
        payObj.put("notify_url", payConfig.getCallbackUrl());
        payObj.put("time_expire", timeExpire);
        payObj.put("amount", amountObj);
        //回调携带
        payObj.put("attach", "{\"accountNo\":" + payInfoVO.getAccountNo() + "}");
        // 处理请求body参数
        String body = payObj.toJSONString();
        log.debug("请求参数:{}",body);
        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept","application/json");
        httpPost.setEntity(entity);
        String result = "";
        try(CloseableHttpResponse response = wechatPayClient.execute(httpPost)){
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.debug("下单响应码:{},响应体:{}",statusCode,responseStr);
            if(statusCode == HttpStatus.OK.value()){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.containsKey("code_url")){
                    result = jsonObject.getString("code_url");
                }
            }else {
                log.error("下单响应失败:{},响应体:{}",statusCode,responseStr);
            }
        }catch (Exception e){
            log.error("微信支付响应异常:{}",e);
        }
        return result;
    }


    /**
     * 微信退款
     * @param payInfoVO
     * @return
     */
    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }



    /**
     * 微信支付查询订单状态
     * @param payInfoVO
     * @return
     */
    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        String outTradeNo = payInfoVO.getOutTradeNo();
        String url = String.format(WechatPayApi.NATIVE_QUERY,outTradeNo,payConfig.getMchId());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept","application/json");
        String result = "";
        try(CloseableHttpResponse response = wechatPayClient.execute(httpGet)){
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.debug("查询响应码:{},响应体:{}",statusCode,responseStr);
            if(statusCode == HttpStatus.OK.value()){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.containsKey("trade_state")){
                    result = jsonObject.getString("trade_state");
                }
            }else {
                log.error("查询支付状态响应失败:{},响应体:{}",statusCode,responseStr);
            }
        }catch (Exception e){
            log.error("微信支付响应异常:{}",e);
        }
        return result;
    }


    /**
     * 关闭订单
     * @param payInfoVO
     * @return
     */
    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        String outTradeNo = payInfoVO.getOutTradeNo();
        JSONObject payObj = new JSONObject();
        payObj.put("mchid",payConfig.getMchId());
        String body = payObj.toJSONString();
        log.debug("请求参数:{}",body);
        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/json");
        String url = String.format(WechatPayApi.NATIVE_CLOSE_ORDER,outTradeNo);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept","application/json");
        httpPost.setEntity(entity);
        String result = "";
        try(CloseableHttpResponse response = wechatPayClient.execute(httpPost)){
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            log.debug("关闭订单响应码:{},无响应体",statusCode);
            //响应码为204表示正常
            if(statusCode == HttpStatus.NO_CONTENT.value()){
                result = "CLOSE_SUCCESS";
            }
        }catch (Exception e){
            log.error("微信支付响应异常:{}",e);
        }
        return result;
    }
}