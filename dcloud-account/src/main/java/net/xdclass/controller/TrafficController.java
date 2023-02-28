package net.xdclass.controller;

import net.xdclass.controller.request.TrafficPageRequest;
import net.xdclass.controller.request.UseTrafficRequest;
import net.xdclass.service.TrafficService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.TrafficVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-02-07
 */

@RestController
@RequestMapping("/api/traffic/v1")
public class TrafficController {

    @Autowired
    private TrafficService trafficService;

    @Value("${rpc.token}")
    private String rpcToken;


    /**
     * 扣减流量包
     *
     * @param useTrafficRequest
     * @param request
     * @return
     */
    @PostMapping("reduce")
    public JsonData useTraffic(@RequestBody UseTrafficRequest useTrafficRequest, HttpServletRequest request){
        String requestToken = request.getHeader("rpc-token");
        if(rpcToken.equalsIgnoreCase(requestToken)){
            //具体使用流量包逻辑
            JsonData jsonData = trafficService.reduce(useTrafficRequest);
            return jsonData;
        }else {
            return JsonData.buildError("非法访问");
        }
    }



    /**
     * 分页查询流量包列表，查看可用的流量包
     * @param request
     * @return
     */
    @RequestMapping("page")
    public JsonData pageAvailable(@RequestBody TrafficPageRequest request){
        Map<String,Object> pageMap = trafficService.pageAvailable(request);
        return JsonData.buildSuccess(pageMap);
    }


    /**
     * 查找某个流量包详情
     * @param trafficId
     * @return
     */
    @GetMapping("/detail/{trafficId}")
    public JsonData detail(@PathVariable("trafficId") long trafficId){
        TrafficVO trafficVO = trafficService.detail(trafficId);
        return JsonData.buildSuccess(trafficVO);
    }
}