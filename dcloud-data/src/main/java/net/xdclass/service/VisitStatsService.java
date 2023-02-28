package net.xdclass.service;

import net.xdclass.controller.request.*;
import net.xdclass.vo.VisitStatsVO;

import java.util.List;
import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-02-19
 */


public interface VisitStatsService {

    /**
     * 访问记录分页
     * @param request
     * @return
     */
    Map<String,Object> pageVisitRecord(VisitRecordPageRequest request);


    /**
     * 查询时间范围内的，地区访问分布
     * @param request
     * @return
     */
    List<VisitStatsVO> queryRegionWithDay(RegionQueryRequest request);


    /**
     * 访问趋势图
     * @param request
     * @return
     */
    List<VisitStatsVO> queryVisitTrend(VisitTrendQueryRequest request);


    /**
     * 高频rerere统计
     * @param request
     * @return
     */
    List<VisitStatsVO> queryFrequentSource(FrequentSourceRequset request);


    /**
     * 查询设备访问分布情况
     * @param request
     * @return
     */
    Map<String,List<VisitStatsVO>> queryDeviceInfo(QueryDeviceRequest request);
}
