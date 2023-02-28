package net.xdclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.VisitStatsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author 刘森飚
 * @since 2023-02-19
 */


public interface VisitStatsMapper extends BaseMapper<VisitStatsDO> {


    /**
     * 分页查询
     * @param code
     * @param accountNo
     * @param from
     * @param size
     * @return
     */
    List<VisitStatsDO> pageVisitRecord(@Param("code") String code, @Param("accountNo") Long accountNo,
                                       @Param("from") int from, @Param("size") int size);

    /**
     * 计算总条数
     * @param code
     * @param accountNo
     * @return
     */
    int countTotal(@Param("code") String code, @Param("accountNo") Long accountNo);


    /**
     * 根据时间范围查询地区访问分布
     * @param code
     * @param startTime
     * @param endTime
     * @param accountNo
     * @return
     */
    List<VisitStatsDO> queryRegionVisitStatsWithDay(@Param("code") String code,
                                                    @Param("accountNo") Long accountNo,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime);


    /**
     * 查询时间范围内的访问趋势图
     * @param code
     * @param startTime
     * @param endTime
     * @param accountNo
     * @return
     */
    List<VisitStatsDO> queryVisitTrendWithMultiDay(@Param("code") String code,
                                                   @Param("accountNo") Long accountNo,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime);



    /**
     * 查询时间范围内的访问趋势图 小时级别
     * @param code
     * @param accountNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<VisitStatsDO> queryVisitTrendWithHour(@Param("code") String code,
                                               @Param("accountNo") Long accountNo,
                                               @Param("startTime") String startTime);


    /**
     * 查询时间范围内的访问趋势图 分钟级别
     * @param code
     * @param accountNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<VisitStatsDO> queryVisitTrendWithMinute(@Param("code") String code,
                                                 @Param("accountNo") Long accountNo,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);



    /**
     * 查询高频访问来源
     * @param code
     * @param accountNo
     * @param startTime
     * @param endTime
     * @param size
     * @return
     */
    List<VisitStatsDO> queryFrequentSource(@Param("code") String code,
                                           @Param("accountNo") Long accountNo,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime,
                                           @Param("size") int size);


    /**
     * 查询设备类型
     * @param code
     * @param accountNo
     * @param startTime
     * @param endTime
     * @param field
     * @return
     */
    List<VisitStatsDO> queryDeviceInfo(@Param("code") String code,
                                       @Param("accountNo") Long accountNo,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime,
                                       @Param("field") String field);
}
