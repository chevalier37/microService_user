package com.tourguide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tourguide.helper.InternalTestHelper;
import com.tourguide.model.Location;
import com.tourguide.model.User;
import com.tourguide.model.VisitedLocation;
import com.tourguide.proxy.MicroServiceRewardProxy;
import com.tourguide.tracker.Tracker;

@Service
public class UserService {

	@Autowired
	MicroServiceRewardProxy rewardProxy;

	private Logger logger = LoggerFactory.getLogger(UserService.class);
	public final Tracker tracker;

	public UserService() {
		initializeInternalUsers();
		tracker = new Tracker(this);
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: null;
		return visitedLocation;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = getUserLocation(user);
		user.addToVisitedLocations(visitedLocation);
		rewardProxy.calculateRewards(user);
		return visitedLocation;
	}

	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {

		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			// created by JB
			int nbrAdult = ThreadLocalRandom.current().nextInt(1, 4);
			int nbrChild = ThreadLocalRandom.current().nextInt(1, 4);
			int nbrNight = ThreadLocalRandom.current().nextInt(1, 15);
			int nbrTicket = ThreadLocalRandom.current().nextInt(1, 4);

			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			// created by JB
			// UserPreferences userPreferences = new UserPreferences(nbrNight, nbrTicket,
			// nbrAdult, nbrChild);
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
