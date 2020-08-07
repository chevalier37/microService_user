package com.tourguide;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tourguide.controller.UserController;
import com.tourguide.proxy.MicroServiceRewardProxy;
import com.tourguide.service.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	MicroServiceRewardProxy rewardProxy;

	@Test
	public void index() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getUser/internalUser1")).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getUserLocation() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserLocation").param("userName", "internalUser1"))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getAllUsers() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getAllUsers")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void trackUserLocation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/user/trackUserLocation/internalUser1").param("userName", "internalUser1"))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getTripDeals() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/user/getTripDeals/internalUser1").param("userName", "internalUser1"))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getAllCurrentLocations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getAllCurrentLocations")).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getLocation() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/user/getLocation").param("userName", "internalUser1"))
				.andDo(print()).andExpect(status().isOk());
	}

}
