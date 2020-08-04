package com.tourguide;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tourguide.model.Attraction;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.VisitedLocation;
import com.tourguide.proxy.MicroServiceGpsProxy;
import com.tourguide.proxy.MicroServiceRewardProxy;
import com.tourguide.service.UserService;

@SpringBootTest
public class PerformanceTest {

	private static final Logger logger = LogManager.getRootLogger();

	@Autowired
	UserService userService;

	@Autowired
	MicroServiceRewardProxy rewardProxy;

	@Autowired
	MicroServiceGpsProxy gpsProxy;

	@Ignore
	@Test
	public void highVolumeTrackLocation() {

		List<User> allUsers = userService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (User user : allUsers) {
			userService.trackUserLocation(user);
		}
		stopWatch.stop();

		logger.info("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
				+ " seconds. + size " + allUsers.size());

		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));

	}

	@Ignore
	@Test
	public void highVolumeGetRewards() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Attraction attraction = gpsProxy.getAttractions().get(0);

		List<User> allUsers = new ArrayList<>();

		allUsers = userService.getAllUsers();

		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		allUsers.forEach(u -> rewardProxy.calculateRewards(u.getUserName()));

		for (User user : allUsers) {
			user.addUserReward(new UserReward(user.getLastVisitedLocation(), attraction, 100));
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();

		logger.info("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
				+ " seconds. + size " + allUsers.size());

		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
