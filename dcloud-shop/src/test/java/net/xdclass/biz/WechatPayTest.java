package net.xdclass.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShopApplication;
import net.xdclass.config.PayBeanConfig;
import net.xdclass.config.WechatPayApi;
import net.xdclass.config.WechatPayConfig;
import net.xdclass.util.CommonUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author 刘森飚
 * @since 2023-02-03
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)

@Slf4j
public class WechatPayTest {

    @Autowired
    private PayBeanConfig payBeanConfig;


    @Autowired
    private WechatPayConfig payConfig;

    @Autowired
    private CloseableHttpClient wechatPayClient;


    /**
     * 商户私钥证书读取
     *
     * @throws IOException
     */
    @Test
    public void testLoadPrivateKey() throws IOException {

        log.info(payBeanConfig.getPrivateKey().getAlgorithm());

    }

    /**
     * 快速验证统一下单接口
     *
     * @throws IOException
     */
    @Test
    public void testNativeOrder() throws IOException {

        String outTradeNo = CommonUtil.getStringNumRandom(32);

        /**
         * {
         * 	"mchid": "1900006XXX",
         * 	"out_trade_no": "native12177525012014070332333",
         * 	"appid": "wxdace645e0bc2cXXX",
         * 	"description": "Image形象店-深圳腾大-QQ公仔",
         * 	"notify_url": "https://weixin.qq.com/",
         * 	"amount": {
         * 		"total": 1,
         * 		"currency": "CNY"
         *        }
         * }
         */
        JSONObject payObj = new JSONObject();
        payObj.put("mchid", payConfig.getMchId());
        payObj.put("out_trade_no", outTradeNo);
        payObj.put("appid", payConfig.getWxPayAppid());
        payObj.put("description", "刘森飚下单啦！！！");
        payObj.put("notify_url", payConfig.getCallbackUrl());

        //订单总金额，单位为分。
        JSONObject amountObj = new JSONObject();
        amountObj.put("total", 100);
        amountObj.put("currency", "CNY");
        payObj.put("amount", amountObj);
        //附属参数，可以用在回调
        payObj.put("attach", "{\"accountNo\":" + 888 + "}");
        String body = payObj.toJSONString();
        log.info("请求参数:{}", body);
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);
        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost)) {
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.info("下单响应码:{},响应体:{}", statusCode, responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据商户号订单号查询订单支付状态
     *
     * @throws IOException
     */
    @Test
    public void testNativeQuery() throws IOException {
        String outTradeNo = "gQZeW356sREDGQ1r8drDHBSHaoYX7irb";
        String url = String.format(WechatPayApi.NATIVE_QUERY, outTradeNo, payConfig.getMchId());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        try (CloseableHttpResponse response = wechatPayClient.execute(httpGet)) {
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.info("查询响应码:{},响应体:{}", statusCode, responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭订单
     *
     * @throws IOException
     */
    @Test
    public void testNativeCloseOrder() throws IOException {
        String outTradeNo = "gQZeW356sREDGQ1r8drDHBSHaoYX7irb";
        JSONObject payObj = new JSONObject();
        payObj.put("mchid", payConfig.getMchId());
        String body = payObj.toJSONString();
        log.info("请求参数:{}", body);
        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");
        String url = String.format(WechatPayApi.NATIVE_CLOSE_ORDER, outTradeNo);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);
        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost)) {
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("关闭订单响应码:{},无响应体", statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 申请退款
     * @throws IOException
     */
    @Test
    public void testNativeRefundOrder() throws IOException {
        //统一下单订单号
        String outTradeNo = "xzxrQAW97qgopIKV9X9x7h3puozzQja9";
        //退款订单号
        String refundNo = CommonUtil.getStringNumRandom(32);
        // 请求body参数
        JSONObject refundObj = new JSONObject();
        //订单号
        refundObj.put("out_trade_no", outTradeNo);
        //退款单编号，商户系统内部的退款单号，商户系统内部唯一，
        // 只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔
        refundObj.put("out_refund_no", refundNo);
        refundObj.put("reason", "商品已售完");
        refundObj.put("notify_url", payConfig.getCallbackUrl());
        JSONObject amountObj = new JSONObject();
        //退款金额
        amountObj.put("refund", 10);
        //实际支付的总金额
        amountObj.put("total", 100);
        amountObj.put("currency", "CNY");
        refundObj.put("amount", amountObj);
        String body = refundObj.toJSONString();

        log.info("请求参数:{}", body);
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_REFUND_ORDER);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);
        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost)) {
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.info("申请订单退款响应码:{},响应体:{}", statusCode, responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 订单退款状态查询
     * @throws IOException
     */
    @Test
    public void testNativeRefundQuery() throws IOException {
        String refundNo = "92bmNIEHgO5MMBYzlSRosW8TzOy6Du4v";
        String url = String.format(WechatPayApi.NATIVE_REFUND_QUERY,refundNo);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept","application/json");
        try(CloseableHttpResponse response = wechatPayClient.execute(httpGet)){
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());
            log.info("查询订单退款 响应码:{},响应体:{}",statusCode,responseStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

