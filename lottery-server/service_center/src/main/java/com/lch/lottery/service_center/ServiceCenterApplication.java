package com.lch.lottery.service_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer //标识此工程是一个EurekaServer
@SpringBootApplication
public class ServiceCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCenterApplication.class, args);
	}

}
