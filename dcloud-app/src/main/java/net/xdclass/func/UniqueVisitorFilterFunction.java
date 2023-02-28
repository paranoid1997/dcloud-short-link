package net.xdclass.func;

import com.alibaba.fastjson.JSONObject;
import net.xdclass.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

/**
 * @author 刘森飚
 * @since 2023-02-16
 */

public class UniqueVisitorFilterFunction extends RichFilterFunction<JSONObject> {

    /**
     * 最新访问状态
     */
    private ValueState<String> lastVisitDateState = null;


    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<String> visitDateStateDes = new ValueStateDescriptor<>
                ("visitDateState", String.class);
        //统计UV
        //设置过期时间，一天就过期
    StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.days(1)).build();
        visitDateStateDes.enableTimeToLive(stateTtlConfig);
        this.lastVisitDateState = getRuntimeContext().getState(visitDateStateDes);

}




    @Override
    public void close() throws Exception {
        super.close();
    }


    /**
     * UV去重
     * @param jsonObj
     * @return
     * @throws Exception
     */
    @Override
    public boolean filter(JSONObject jsonObj) throws Exception {
        //获取当前访问时间
        Long visitTime = jsonObj.getLong("visitTime");
        String udid = jsonObj.getString("udid");
        //当前访问时间
        String currentVisitDate = TimeUtil.format(visitTime);
        //获取上次的状态访问时间
        String lastVisitDate = lastVisitDateState.value();
        ////用当前页面的访问时间和状态时间进行对比
        if(StringUtils.isNotBlank(lastVisitDate) && currentVisitDate.equalsIgnoreCase(lastVisitDate)){
            System.out.println(udid + " 已经在 " + currentVisitDate + "时间访问过");
            return false;
        }else {
            System.out.println(udid +" 在 " + currentVisitDate + "时间初次访问");
            lastVisitDateState.update(currentVisitDate);
            return true;
        }
    }
}