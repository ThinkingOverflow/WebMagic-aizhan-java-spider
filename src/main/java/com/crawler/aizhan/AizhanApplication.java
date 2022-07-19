package com.crawler.aizhan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.crawler.aizhan.mapper")
public class AizhanApplication {

    public static void main(String[] args) {
        SpringApplication.run(AizhanApplication.class, args);
    }

}
