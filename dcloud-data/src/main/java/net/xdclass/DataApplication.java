package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 刘森飚
 * @since 2023-02-19
 */

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("net.xdclass.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class DataApplication {

    public static void main(String [] args){
        SpringApplication.run(DataApplication.class,args);
    }
}
