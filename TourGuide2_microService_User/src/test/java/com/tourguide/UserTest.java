package com.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tourguide.helper.InternalTestHelper;
import com.tourguide.model.Location;
import com.tourguide.model.Provider;
import com.tourguide.model.User;
import com.tourguide.model.VisitedLocation;
import com.tourguide.proxy.MicroServiceGpsProxy;
import com.tourguide.proxy.MicroServiceRewardProxy;
import com.tourguide.service.UserService;

@SpringBootTest
public class UserTest {

	private static final Logger logger = LogManager.getRootLogger();

	@Autowired
	UserService userService;

	@Autowired
	MicroServiceRewardProxy rewardProxy;

	@Autowired
	MicroServiceGpsProxy gpsProxy;

	@Test
	public void getUserTest() {
		User user = userService.getUser("internalUser1");
		assertTrue(user.getUserName().equals("internalUser1"));

	}

	@Test
	public void getUserLocation() {
		User user = userService.getUser("internalUser1");
		VisitedLocation visitedLocation = userService.trackUserLocation(user);
		userService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));

	}

	@Test
	public void addUser() {
		UserService userService = new UserService();
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		userService.addUser(user);
		userService.addUser(user2);

		User retrivedUser = userService.getUser(user.getUserName());
		User retrivedUser2 = userService.getUser(user2.getUserName());

		userService.tracker.stopTracking();

		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {
		UserService userService = new UserService();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		userService.addUser(user);
		userService.addUser(user2);

		List<User> allUsers = userService.getAllUsers();

		userService.tracker.stopTracking();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = userService.trackUserLocation(user);
		userService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getTripDeals() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<Provider> providers = userService.getTripDeals(user);
		assertEquals(5, providers.size());
	}

	@Test
	public void getAllCurrentLocations() {
		Map<String, Location> getAllCurrentLocations = userService.getAllCurrentLocations();
		int users = InternalTestHelper.getInternalUserNumber();
		assertEquals(users, getAllCurrentLocations.size());
	}

}
