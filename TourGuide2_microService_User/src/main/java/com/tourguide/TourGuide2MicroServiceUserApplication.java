package com.tourguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.tourguide")
@EnableDiscoveryClient
public class TourGuide2MicroServiceUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourGuide2MicroServiceUserApplication.class, args);
	}

}
