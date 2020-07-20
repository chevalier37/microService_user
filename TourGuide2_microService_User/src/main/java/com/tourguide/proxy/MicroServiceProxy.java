package com.tourguide.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "microservice-gpsUtil", url = "localhost:9001")
public interface MicroServiceProxy {


	@GetMapping("/getGpsUtil") 
	Object getGpsUtil();

}