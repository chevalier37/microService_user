package com.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tourguide.helper.InternalTestHelper;
import com.tourguide.model.User;
import com.tourguide.service.UserService;

@SpringBootTest
public class PerformanceTest {

	private static final Logger logger = LogManager.getRootLogger();

	@Autowired
	UserService userService;

	@Test
	public void highVolumeTrackLocation() {

		InternalTestHelper.setInternalUserNumber(100000);
		UserService userService = new UserService();

		List<User> allUsers = userService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (User user : allUsers) {
			userService.trackUserLocation(user);
		}
		stopWatch.stop();
		userService.tracker.stopTracking();

		logger.info("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
				+ " seconds.");

		assertEquals(allUsers.size(), 0);
	}

}
