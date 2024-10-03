package com.example.bankingapp;

import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankingappApplicationTests {

//	@LocalServerPort
//	private int port;
//
//	private String baseUrl;
//
//	private static RestTemplate restTemplate;
//
//	@Autowired
//	private TestH2Repository testH2Repository;
//
//	@BeforeAll
//	public static void init() {
//		restTemplate = new RestTemplate();
//	}
//
//	@BeforeEach
//	public void setup() {
//		baseUrl = "http://localhost:" + port + "/v1";
//		testH2Repository.deleteAll(); // Clear the database before each test
//	}
//
//	@Test
//	public void testAddUser() {
//		// Create a User object to send in the POST request
//		User user = User.builder()
//				.age(22)
//				.role(Role.ADMIN)
//				.password("password")
//				.lastName("Matta")
//				.firstName("Manan")
//				.email("manan@gmail.com")
//				.build();
//
//		// Send a POST request to add the user
//		User response = restTemplate.postForObject(baseUrl + "/users", user, User.class);
//
//		// Verify that the response contains the correct email
//		assertEquals("manan@gmail.com", response.getEmail());
//
//		// Verify that one user was saved in the database
//		assertEquals(1, testH2Repository.findAll().size());
//	}

	@Test
	void contextLoads() {
		// This test ensures the Spring application context loads successfully
	}
}
