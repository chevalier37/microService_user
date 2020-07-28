package com.tourguide.proxy;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.tourguide.model.Attraction;

@FeignClient(name = "microservice-gpsUtil")
@RibbonClient(name = "microservice-gpsUtil")
public interface MicroServiceGpsProxy {

	@GetMapping("/getAttractions")
	List<Attraction> getAttractions();

}