package net.xdclass.dwm;

import net.xdclass.func.AsyncLocaltionRequestFunction;
import net.xdclass.func.DeviceMapFunction;
import net.xdclass.func.LocationMapFunction;
import net.xdclass.model.ShortLinkWideDO;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.concurrent.TimeUnit;

/**
 * @author 刘森飚
 * @since 2023-02-16
 */


public class DwmShortLinWideApp {

    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_short_link_group";


    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_link_visit_topic";


    public static void main(String [] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(1);
        //DataStream<String> ds =  env.socketTextStream("10.0.0.67",8888);
        // 1、获取流
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        //2、格式装换，补齐设备信息
        SingleOutputStreamOperator<ShortLinkWideDO> deviceWideDS = ds.map(new DeviceMapFunction());
        deviceWideDS.print("设备信息宽表补齐");

        //3、补齐地理位置信息
        SingleOutputStreamOperator<String> shortLinkWideDS = AsyncDataStream.unorderedWait
                (deviceWideDS, new AsyncLocaltionRequestFunction(), 1000, TimeUnit.MILLISECONDS, 200);

        shortLinkWideDS.print("地理位置信息宽表补齐");
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);

        //4、将sink写到dwm层，kafka存储
        shortLinkWideDS.addSink(kafkaProducer);
        env.execute();
    }
}
