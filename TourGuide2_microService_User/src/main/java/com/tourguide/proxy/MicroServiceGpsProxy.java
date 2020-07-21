package com.tourguide.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "microservice-gpsUtil")
@RibbonClient(name = "microservice-gpsUtil")
public interface MicroServiceGpsProxy {

	@GetMapping("/getGpsUtil")
	Object getGpsUtil();

}