package net.xdclass.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xdclass.model.TrafficDO;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */

public interface TrafficManager {

    /**
     * 新增流量包
     * @param trafficDO
     * @return
     */
    int add(TrafficDO trafficDO);


    /**
     * 分页查询可用的流量包
     * @param page
     * @param size
     * @param accountNo
     * @return
     */
    IPage<TrafficDO> pageAvailable(int page, int size, Long accountNo);


    /**
     * 查找详情
     * @param trafficId
     * @param accountNo
     * @return
     */
    TrafficDO findByIdAndAccountNo(Long trafficId, Long accountNo);


    /**
     * 删除过期流量包
     * @return
     */
    boolean deleteExpireTraffic();


    /**
     * 查找可用的短链流量包(未过期)
     * 免费流量包和付费流量包
     * @param accountNo
     * @return
     */
    List<TrafficDO> selectAvailableTraffics(Long accountNo);


    /**
     * 给某个流量包增加使用次数
     *
     * @param trafficId
     * @param accountNo
     * @param usedTimes
     * @return
     */
    int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) ;



    /**
     * 恢复流量包使用当天次数
     * @param accountNo
     * @param trafficId
     * @param useTimes
     */
    int releaseUsedTimes(Long accountNo, Long trafficId, Integer useTimes,String useDateStr);


    /**
     * 批量更新流量包使用次数为0
     * @param accountNo
     * @param unUpdatedTrafficIds
     */
    int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds);

}
