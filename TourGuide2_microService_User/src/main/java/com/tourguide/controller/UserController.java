package com.tourguide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;
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
	public String getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = userService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
	}

	// TODO: Change this method to no longer return a List of Attractions.
	// Instead: Get the closest five tourist attractions to the user - no matter how
	// far away they are.
	// Return a new JSON object that contains:
	// Name of Tourist attraction,
	// Tourist attractions lat/long,
	// The user's location lat/long,
	// The distance in miles between the user's location and each of the
	// attractions.
	// The reward points for visiting each Attraction.
	// Note: Attraction reward points can be gathered from RewardsCentral

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

}
