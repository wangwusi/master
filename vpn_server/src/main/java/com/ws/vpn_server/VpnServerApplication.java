package com.ws.vpn_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringCloudApplication
@ComponentScan(basePackages = "com.ws")
@MapperScan("com.ws.vpn_server.dao")
public class VpnServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpnServerApplication.class, args);
    }

}
