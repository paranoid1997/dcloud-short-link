package net.xdclass.manager;

import net.xdclass.model.TrafficTaskDO;

/**
 * @author 刘森飚
 * @since 2023-02-11
 */

public interface TrafficTaskManager {

    /**
     * 添加流量包任务
     * @param trafficTaskDO
     * @return
     */
    int add(TrafficTaskDO trafficTaskDO);

    /**
     * 查找流量包任务
     * @param id
     * @param accountNo
     * @return
     */
    TrafficTaskDO findByIdAndAccountNo(Long id, Long accountNo);


    /**
     * 删除流量包任务
     * @param id
     * @param accountNo
     * @return
     */
    int deleteByIdAndAccountNo(Long id, Long accountNo);

}
