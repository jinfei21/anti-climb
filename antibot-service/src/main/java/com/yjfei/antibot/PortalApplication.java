package com.yjfei.antibot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = "com.yjfei.antibot.dao", annotationClass = Repository.class)
@EnableSwagger2
@EnableFeignClients(basePackages = { "com.yjfei" })
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
@SuppressWarnings("all")
public class PortalApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(PortalApplication.class, args);
            log.info("antibot portal server start success!");
        }catch (Throwable t){
            log.error("antibot portal server start error!");
        }
    }
}
