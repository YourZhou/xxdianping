package com.xxdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xxdp.mapper")
@SpringBootApplication
public class XxdianpingApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxdianpingApplication.class, args);
    }

}
