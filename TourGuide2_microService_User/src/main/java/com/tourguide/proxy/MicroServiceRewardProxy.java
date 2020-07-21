package com.tourguide.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tourguide.model.User;

@FeignClient(name = "microservice-reward")
@RibbonClient(name = "microservice-reward")
public interface MicroServiceRewardProxy {

	@PostMapping("/calculateRewards")
	void calculateRewards(@RequestBody User user);

}
