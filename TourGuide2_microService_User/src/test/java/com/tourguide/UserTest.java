package com.tourguide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tourguide.service.UserService;

@SpringBootTest
public class UserTest {

	@Autowired
	UserService userService;

	/*
	 * @Test public void getUserLocation() {
	 * InternalTestHelper.setInternalUserNumber(0); User user = new
	 * User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com"); VisitedLocation
	 * visitedLocation = userService.trackUserLocation(user);
	 * userService.tracker.stopTracking();
	 * assertTrue(visitedLocation.userId.equals(user.getUserId())); }
	 */

}
