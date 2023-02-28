package net.xdclass.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 刘森飚
 * @since 2023-02-08
 */

@Component
@Slf4j
public class TrafficJobHandler {


    @Autowired
    private TrafficService trafficService;

    /**
     * 过期流量包处理
     * @param param
     * @return
     */
    @XxlJob(value = "trafficExpiredHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){
        log.info("execute 任务方法触发成功,删除过期流量包");
        trafficService.deleteExpireTraffic();
        return ReturnT.SUCCESS;
    }

    private void init(){

        log.info("MyJobHandler init >>>>>");
    }

    private void destroy(){
        log.info(" MyJobHandler destroy >>>>>");
    }

}