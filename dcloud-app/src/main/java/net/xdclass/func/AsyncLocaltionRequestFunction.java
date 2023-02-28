package net.xdclass.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ShortLinkWideDO;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 刘森飚
 * @since 2023-02-16
 */

@Slf4j
public class AsyncLocaltionRequestFunction extends RichAsyncFunction<ShortLinkWideDO,String> {

    private static final String IP_PARSE_URL =
            "https://restapi.amap.com/v3/ip?ip=%s&output=json&key=5a7de5d119a47da428dfe7cc26d550d7";

    private CloseableHttpAsyncClient httpAsyncClient;


    /**
     * 超时处理
     * @param input
     * @param resultFuture
     * @throws Exception
     */
    @Override
    public void timeout(ShortLinkWideDO input, ResultFuture<String> resultFuture) throws Exception {
        resultFuture.complete(Collections.singleton(null));
    }



    /**
     * 初始化异步客户端
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        this.httpAsyncClient = createAsyncHttpClient();
    }


    /**
     * 关闭异步客户端
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if(httpAsyncClient!=null){
            httpAsyncClient.close();
        }
    }


    /**
     * 异步解析地理信息
     * @param shortLinkWideDO
     * @param resultFuture
     * @throws Exception
     */
    @Override
    public void asyncInvoke(ShortLinkWideDO shortLinkWideDO, ResultFuture<String> resultFuture) throws Exception {
        String ip = shortLinkWideDO.getIp();
        String url = String.format(IP_PARSE_URL,ip);
        HttpGet httpGet = new HttpGet(url);
        Future<HttpResponse> future = httpAsyncClient.execute(httpGet, null);
        CompletableFuture<ShortLinkWideDO> completableFuture = CompletableFuture.
                supplyAsync(new Supplier<ShortLinkWideDO>() {
                    @Override
                    public ShortLinkWideDO get() {
                        try {
                            HttpResponse response = future.get();
                            int statusCode = response.getStatusLine().getStatusCode();
                            if (statusCode == HttpStatus.SC_OK) {
                                HttpEntity entity = response.getEntity();
                                String result = EntityUtils.toString(entity, "UTF-8");
                                JSONObject locationObj = JSON.parseObject(result);
                                String city = locationObj.getString("city");
                                String province = locationObj.getString("province");
                                shortLinkWideDO.setProvince(province);
                                shortLinkWideDO.setCity(city);
                                return shortLinkWideDO;
                            }
                        } catch (InterruptedException | ExecutionException | IOException e) {
                            log.error("ip解析错误,value={},msg={}", shortLinkWideDO, e.getMessage());
                        }
                        shortLinkWideDO.setProvince("-");
                        shortLinkWideDO.setCity("-");
                        return shortLinkWideDO;
                    }
                });
        completableFuture.thenAccept(new Consumer<ShortLinkWideDO>() {
            @Override
            public void accept(ShortLinkWideDO shortLinkWideDO) {
                resultFuture.complete(Collections.singleton(JSON.toJSONString(shortLinkWideDO)));
            }
        });
    }



    /**
     * 创建异步客户端
     * @return
     */
    private CloseableHttpAsyncClient createAsyncHttpClient() {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    //返回数据的超时时间
                    .setSocketTimeout(20000)
                    //连接上服务器的超时时间
                    .setConnectTimeout(10000)
                    //从连接池中获取连接的超时时间
                    .setConnectionRequestTimeout(1000)
                    .build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            //设置连接池最大是500个连接
            connManager.setMaxTotal(500);
            //MaxPerRoute是对maxtotal的细分，每个主机的并发最大是300，route是指域名
            connManager.setDefaultMaxPerRoute(300);
            CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            httpClient.start();
            return httpClient;
        } catch (IOReactorException e) {
            log.error("初始化 CloseableHttpAsyncClient异常:{}",e.getMessage());
            return null;
        }
    }
}