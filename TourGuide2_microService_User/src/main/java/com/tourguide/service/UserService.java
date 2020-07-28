package com.tourguide.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tourguide.helper.InternalTestHelper;
import com.tourguide.model.Location;
import com.tourguide.model.Provider;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.VisitedLocation;
import com.tourguide.proxy.MicroServiceRewardProxy;
import com.tourguide.tracker.Tracker;

import tripPricer.TripPricer;

@Service
public class UserService {

	@Autowired
	MicroServiceRewardProxy rewardProxy;

	private Logger logger = LoggerFactory.getLogger(UserService.class);
	public final Tracker tracker;
	private final TripPricer tripPricer = new TripPricer();

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

	public void addUserReward(User user) {
		User userGet = getUser(user.getUserName());
		for (UserReward userReward : user.getUserRewards()) {
			userGet.addUserReward(userReward);
		}
	}

	public VisitedLocation getUserLocation(User user) {
		return (!user.getVisitedLocations().isEmpty()) ? user.getLastVisitedLocation()
				: new VisitedLocation(user.getUserId(),
						new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime());
	}

	@Async("asyncExecutor")
	public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
		VisitedLocation visitedLocation = getUserLocation(user);
		user.addToVisitedLocations(visitedLocation);
		rewardProxy.calculateRewards(user.getUserName());
		return CompletableFuture.completedFuture(visitedLocation);
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

		List<tripPricer.Provider> tripPricerProviders = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);

		List<Provider> modelProviders = tripPricerProviders.stream()
				.map(provider -> new Provider(provider.tripId, provider.name, provider.price)).collect(toList());

		user.setTripDeals(modelProviders);
		return modelProviders;
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
