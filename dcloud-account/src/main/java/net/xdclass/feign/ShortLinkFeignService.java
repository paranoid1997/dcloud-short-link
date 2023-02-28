package net.xdclass.feign;

import net.xdclass.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author 刘森飚
 * @since 2023-02-12
 */

@FeignClient(name = "dcloud-link-service")
public interface ShortLinkFeignService {


    /**
     * 检查短链是否存在
     * @param shortLinkCode
     * @return
     */
    @GetMapping(value = "/api/link/v1/check",headers = {"rpc-token=${rpc.token}"})
    JsonData check(@RequestParam("shortLinkCode") String shortLinkCode);

}
