package net.xdclass.service;

import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.controller.request.ProductOrderPageRequest;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.model.EventMessage;
import net.xdclass.util.JsonData;

import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */

public interface ProductOrderService {

    /**
     * 分页接口
     * @param page
     * @param size
     * @param state
     * @return
     */
    Map<String,Object> page(ProductOrderPageRequest orderPageRequest);

    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);


    /**
     * 下单接口
     * @param orderRequest
     */
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);


    /**
     * 关闭订单
     * @param eventMessage
     * @return
     */
    boolean closeProductOrder(EventMessage eventMessage);


    /**
     * 处理微信回调通知
     * @param payType
     * @param paramsMap
     */
    JsonData processOrderCallbackMsg(ProductOrderPayTypeEnum payType, Map<String, String> paramsMap);


    /**
     * 处理订单相关消息
     * @param eventMessage
     */
    void handleProductOrderMessage(EventMessage eventMessage);
}
