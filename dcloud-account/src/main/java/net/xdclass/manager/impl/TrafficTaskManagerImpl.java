package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.TrafficTaskManager;
import net.xdclass.mapper.TrafficTaskMapper;
import net.xdclass.model.TrafficTaskDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 刘森飚
 * @since 2023-02-11
 */


@Component
@Slf4j
public class TrafficTaskManagerImpl implements TrafficTaskManager {

    @Autowired
    private TrafficTaskMapper trafficTaskMapper;


    /**
     * 添加流量包任务
     * @param trafficTaskDO
     * @return
     */
    @Override
    public int add(TrafficTaskDO trafficTaskDO) {
        return trafficTaskMapper.insert(trafficTaskDO);
    }


    /**
     * 查找流量包任务
     * @param id
     * @param accountNo
     * @return
     */
    @Override
    public TrafficTaskDO findByIdAndAccountNo(Long id, Long accountNo) {
        TrafficTaskDO taskDO = trafficTaskMapper.selectOne(new QueryWrapper<TrafficTaskDO>()
                .eq("id", id)
                .eq("account_no", accountNo));
        return taskDO;
    }


    /**
     * 删除流量包任务
     * @param id
     * @param accountNo
     * @return
     */
    @Override
    public int deleteByIdAndAccountNo(Long id, Long accountNo) {
        return trafficTaskMapper.delete(new QueryWrapper<TrafficTaskDO>()
                .eq("id", id)
                .eq("account_no", accountNo));
    }
}
