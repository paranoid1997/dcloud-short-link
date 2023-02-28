package net.xdclass.biz;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.AccountApplication;
import net.xdclass.manager.TrafficManager;
import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Slf4j
public class TrafficTest {

    @Autowired
    private TrafficMapper trafficMapper;

    @Autowired
    private TrafficManager trafficManager;


    @Test
    public void testSendSms() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            TrafficDO trafficDO = new TrafficDO();
            trafficDO.setAccountNo(Long.valueOf(random.nextInt(100)));
            trafficMapper.insert(trafficDO);
        }
    }


    @Test
    public void testDeleteExpiredTraffic() {

        trafficManager.deleteExpireTraffic();
    }


    @Test
    public void testSelectAvailableTraffics(){

        List<TrafficDO> list = trafficManager.selectAvailableTraffics(693100647796441088L);
        list.stream().forEach(obj->{
            log.info(obj.toString());
        });

    }


    @Test
    public void testAddDayUsedTimes(){

        int rows = trafficManager.addDayUsedTimes
                (693100647796441088L,1486221880318595076L,1);

        log.info("rows={}",rows);
    }



    @Test
    public void testReleaseDayUsedTimes(){

        int rows = trafficManager.releaseUsedTimes
                (693100647796441088L,1486221880318595076L,1,"");

        log.info("rows={}",rows);
    }


    @Test
    public void testBatchUpdateUsedTimes(){
        List<Long> list = new ArrayList<>();
        list.add(1486221880318595076L);
        int rows = trafficManager.batchUpdateUsedTimes(693100647796441088L,list);
        log.info("rows={}",rows);

    }

}
