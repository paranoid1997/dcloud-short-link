package net.xdclass.service;

import net.xdclass.controller.request.TrafficPageRequest;
import net.xdclass.controller.request.UseTrafficRequest;
import net.xdclass.model.EventMessage;
import net.xdclass.util.JsonData;
import net.xdclass.vo.TrafficVO;

import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */

public interface TrafficService {


    /**
     * 处理流量消息
     * @param eventMessage
     */
    void handleTrafficMessage(EventMessage eventMessage);


    /**
     * 分页查看可用的流量包
     * @param request
     * @return
     */
    Map<String, Object> pageAvailable(TrafficPageRequest request);


    /**
     * 查询流量包详情
     * @param trafficId
     * @return
     */
    TrafficVO detail(long trafficId);


    /**
     * 删除过期流量包
     * @return
     */
    boolean deleteExpireTraffic();


    /**
     * 扣减流量包
     * @param useTrafficRequest
     * @return
     */
    JsonData reduce(UseTrafficRequest useTrafficRequest);
}
