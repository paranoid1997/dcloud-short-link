package net.xdclass.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 刘森飚
 * @since 2023-02-14
 */


@Slf4j
public class KafkaUtil {


    /**
     * kafka的broker地址
     */
    private static String KAFKA_SERVER = null;

    static {
        Properties properties = new Properties();
        InputStream in = KafkaUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            log.error("加载kafka配置文件失败,{}",e);
        }
        //获取key配置对应的value
        KAFKA_SERVER = properties.getProperty("kafka.servers");
    }



    /**
     * 获取flink的kafka消费者
     * @param topic
     * @param groupId
     * @return
     */
    public static FlinkKafkaConsumer<String> getKafkaConsumer(String topic, String groupId){
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_SERVER);
        return new FlinkKafkaConsumer<String>(topic,new SimpleStringSchema(),props);
    }



    /**
     * 获取flink的kafka生产者
     * @param topic
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer(String topic){
        return new FlinkKafkaProducer<String>(KAFKA_SERVER,topic,new SimpleStringSchema());
    }
}