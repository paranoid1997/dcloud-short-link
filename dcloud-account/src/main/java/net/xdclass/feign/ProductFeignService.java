package net.xdclass.feign;

import net.xdclass.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 刘森飚
 * @since 2023-02-07
 */

@FeignClient(name = "dcloud-shop-service")
public interface ProductFeignService {

    /**
     * 获取流量包商品详情
     * @param productId
     * @return
     */
    @GetMapping("/api/product/v1/detail/{product_id}")
    JsonData detail(@PathVariable("product_id") long productId);

}
