package net.xdclass.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ShortLinkWideDO;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * @author 刘森飚
 * @since 2023-02-16
 */

@Slf4j
public class LocationMapFunction extends RichMapFunction<ShortLinkWideDO,String> {

    private static final String IP_PARSE_URL =
            "https://restapi.amap.com/v3/ip?ip=%s&output=json&key=5a7de5d119a47da428dfe7cc26d550d7";

    private CloseableHttpClient httpClient;


    /**
     * void open(Configuration parameters)：
     * 该方法在 map 函数执行之前被调用，用于执行一些初始化工作，
     * 如获取连接、加载模型等操作。
     * 可以通过参数parameters传递配置信息，如连接地址、模型路径等
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        this.httpClient = createHttpClient();
    }



    /**
     *void close()：
     * 该方法在 map 函数执行完成后被调用，
     * 用于执行一些清理工作，如关闭连接、释放资源等操作。
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if(httpClient != null){
            httpClient.close();
        }
    }



    /**
     * 解析地理位置
     * @param value
     * @return
     * @throws Exception
     */
    @Override
    public String map(ShortLinkWideDO value) throws Exception {
        String ip = value.getIp();
        String url = String.format(IP_PARSE_URL,ip);
        HttpGet httpGet = new HttpGet(url);
        try(CloseableHttpResponse response = httpClient.execute(httpGet)){
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                JSONObject locationObj = JSON.parseObject(result);
                String province = locationObj.getString("province");
                String city = locationObj.getString("city");
                value.setProvince(province);
                value.setCity(city);
            }
        }catch (Exception e){
            log.error("ip解析错误,value={},msg={}",value,e.getMessage());
        }
        return JSON.toJSONString(value);
    }


    /**
     * CloseableHttpClient 是 Java 中一个用于发送 HTTP 请求和接收 HTTP 响应的接口。
     * 它是 Apache HttpClient 库中的一部分，提供了一组功能强大的方法，
     * 使开发人员可以使用各种 HTTP 方法（例如 GET、POST、PUT、DELETE 等）与服务器进行交互
     * @return
     */
    private CloseableHttpClient createHttpClient(){
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //MaxPerRoute是对maxtotal的细分，每个主机的并发最大是300，route是指域名
        connectionManager.setDefaultMaxPerRoute(300);
        //设置连接池最大是500个连接
        connectionManager.setMaxTotal(500);
        /**
         * 只请求 liusenbiao.net,最大并发300
         *
         * 请求 liusenbiao.net,最大并发300
         * 请求 open1024.com,最大并发200
         *
         * //MaxtTotal=400 DefaultMaxPerRoute=200
         * //只连接到http://xdclass.net时，到这个主机的并发最多只有200；而不是400；
         * //而连接到http://xdclass.net 和 http://open1024.com时，到每个主机的并发最多只有200；
         * // 即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
         *
         */
        RequestConfig requestConfig = RequestConfig.custom()
                //返回数据的超时时间
                .setSocketTimeout(20000)
                //连接上服务器的超时时间
                .setConnectTimeout(10000)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(1000)
                .build();
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
        return closeableHttpClient;
    }
}