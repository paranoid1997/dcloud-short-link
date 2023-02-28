package net.xdclass.manager;

import net.xdclass.model.ProductOrderDO;

import java.util.Map;

public interface ProductOrderManager {


    /***
     * 新增
     * @param productOrderDO
     * @return
     */
    int add(ProductOrderDO productOrderDO);


    /**
     * 通过订单号和账号查询
     * @param outTradeNo
     * @param accountNo
     * @return
     */
    ProductOrderDO findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo);


    /**
     * 更新订单状态
     * @param outTradeNo
     * @param accountNo
     * @param newState
     * @param oldState
     * @return
     */
    int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState);


    /**
     * 分页查看订单列表
     * @param page
     * @param size
     * @param accountNo
     * @param state
     * @return
     */
    Map<String,Object> page(int page, int size, Long accountNo, String state);


    /**
     * 删除
     * @param productOrderId
     * @param accountNo
     * @return
     */
    int del(Long productOrderId,Long accountNo);

}
