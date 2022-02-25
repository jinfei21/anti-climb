package com.yjfei.antibot.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = "com.yofei.antibot.stream.dao", annotationClass = Repository.class)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableSwagger2
@Slf4j
@EnableConfigurationProperties
@SuppressWarnings("all")
public class StreamApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(StreamApplication.class, args);
            log.info("antibot stream server start success!");
        }catch (Throwable t){
            log.error("antibot stream server start error!",t);
        }
    }
}
