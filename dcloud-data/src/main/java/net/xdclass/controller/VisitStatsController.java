package net.xdclass.controller;

import net.xdclass.controller.request.*;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.VisitStatsService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.VisitStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-02-19
 */

@RestController
@RequestMapping("/api/visit_stats/v1")
public class VisitStatsController {

    @Autowired
    private VisitStatsService statsService;


    /**
     *  访问记录分页
     * @param request
     * @return
     */
    @RequestMapping("page_record")
    public JsonData pageVisitRecord(@RequestBody VisitRecordPageRequest request){
        //条数限制
        int total = request.getSize() * request.getPage();
        if(total > 1000) {
            return JsonData.buildResult(BizCodeEnum.DATA_OUT_OF_LIMIT_SIZE);
        }
        Map<String,Object> pageResult = statsService.pageVisitRecord(request);
        return JsonData.buildSuccess(pageResult);
    }




    /**
     * 查询时间范围内的，地区访问分布
     * @param request
     * @return
     */
    @RequestMapping("region_day")
    public JsonData queryRegionWithDay(@RequestBody RegionQueryRequest request){
        List<VisitStatsVO> list = statsService.queryRegionWithDay(request);
        return JsonData.buildSuccess(list);
    }



    /**
     * 访问趋势图
     * @param request
     * @return
     */
    @RequestMapping("trend")
    public JsonData queryVisitTrend(@RequestBody VisitTrendQueryRequest request){
        List<VisitStatsVO> list = statsService.queryVisitTrend(request);
        return JsonData.buildSuccess(list);
    }



    /**
     * 高频rerere统计
     * @param request
     * @return
     */
    @RequestMapping("frequent_source")
    public JsonData queryFrequentSource(@RequestBody FrequentSourceRequset request){

        List<VisitStatsVO> list = statsService.queryFrequentSource(request);
        return JsonData.buildSuccess(list);
    }



    /**
     * 查询设备访问分布情况
     * @param request
     * @return
     */
    @RequestMapping("device_info")
    public JsonData queryDeviceInfo(@RequestBody QueryDeviceRequest request){

        Map<String,List<VisitStatsVO>> map = statsService.queryDeviceInfo(request);
        return JsonData.buildSuccess(map);
    }
}
