package com.tourguide.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tourguide.model.Location;
import com.tourguide.model.Provider;
import com.tourguide.model.User;
import com.tourguide.model.VisitedLocation;
import com.tourguide.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	@GetMapping("/getLocation")
	public Location getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = userService.getUserLocation(getUser(userName));
		return visitedLocation.location;
	}

	@GetMapping("/getUser/{userName}")
	public User getUser(@PathVariable("userName") String userName) {
		return userService.getUser(userName);
	}

	@GetMapping("/getUserLocation")
	public VisitedLocation getUserLocation(@RequestParam String userName) {
		return userService.getUserLocation(getUser(userName));
	}

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/trackUserLocation/{userName}")
	public VisitedLocation trackUserLocation(@PathVariable("userName") String userName) {
		User user = userService.getUser(userName);
		return userService.trackUserLocation(user);
	}

	@GetMapping("/getTripDeals/{userName}")
	public List<Provider> getTripDeals(@PathVariable("userName") String userName) {
		User user = userService.getUser(userName);
		return userService.getTripDeals(user);
	}

	@GetMapping("/addUserReward/{userName}")
	public void addUserReward(@PathVariable("userName") String userName) {
		User user = userService.getUser(userName);
		userService.addUserReward(user);
	}

	@GetMapping("/getAllCurrentLocations")
	public Map<String, Location> getAllCurrentLocations() {
		return userService.getAllCurrentLocations();
	}

}
