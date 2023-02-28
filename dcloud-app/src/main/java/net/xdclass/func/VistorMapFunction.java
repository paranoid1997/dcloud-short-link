package net.xdclass.func;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;

/**
 * @author 刘森飚
 * @since 2023-02-14
 */


@Slf4j
public class VistorMapFunction extends RichMapFunction<JSONObject,String> {
    //记录用户的udid访问(访问日期)
    private ValueState<String> newDayVisitorState;



    /**
     * 对状态做初始化
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        newDayVisitorState = getRuntimeContext().getState
                (new ValueStateDescriptor<String>("newDayVisitorState",String.class));
    }


    /**
     * 判断新老访客
     * @param value
     * @return
     * @throws Exception
     */
    @Override
    public String map(JSONObject value) throws Exception {
        //获取之前是否有访问日期
        String beforeDateState = newDayVisitorState.value();
        //获取当前访问时间戳
        Long ts = value.getLong("ts");
        String currentDateStr = TimeUtil.format(ts);
        //判断日期是否为空进行新老访客识别
        if(StringUtils.isNotBlank(beforeDateState)){
            if(beforeDateState.equalsIgnoreCase(currentDateStr)){
                //一样则是老访客
                value.put("is_new",0);
                log.info("老访客:{}", currentDateStr);
            }else {
                //时间不一样，则是新用户,标记1，老访客标记0
                value.put("is_new",1);
                newDayVisitorState.update(currentDateStr);
                log.info("新访客:{}",currentDateStr);
            }
        }else {
            //如果状态为空，则是新用户,标记1，老访客标记0
            value.put("is_new",1);
            newDayVisitorState.update(currentDateStr);
            log.info("新访客:{}",currentDateStr);
        }
        return value.toJSONString();
    }
}