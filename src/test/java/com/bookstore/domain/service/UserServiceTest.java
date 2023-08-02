package com.bookstore.domain.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.exception.ValidationException;
import com.bookstore.domain.model.User;

public class UserServiceTest {

	@Value("${persistence.base.url}")
	private String persistenceBaseUrl;

	private RestTemplate restTemplate;
	private UserService userService;

	@BeforeEach
	public void setUp() {
		restTemplate = mock(RestTemplate.class);
		userService = new UserService(restTemplate);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllUsers() {
		// Prepare the mock response from the persistence microservice
		List<User> mockUsers = new ArrayList<>();
		mockUsers.add(new User(1L, "User 1", "mail@mail.com"));
		mockUsers.add(new User(2L, "User 2", "mail@mail.com"));

		ResponseEntity<List<User>> responseEntity = new ResponseEntity<>(mockUsers, HttpStatus.OK);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/users"), eq(HttpMethod.GET), eq(null),
				any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

		// Call the user service method
		List<User> result = userService.getAllUsers();

		// Assert the result
		assertEquals(mockUsers.size(), result.size());
		assertEquals("User 1", result.get(0).getUsername());
		assertEquals("User 2", result.get(1).getUsername());
	}

	@Test
	public void testGetUserById() {
		// Prepare test data
		Long userId = 1L;

		// Prepare the mock response from the persistence microservice
		User mockUser = new User(userId, "Test User", "mail@mail.com");

		ResponseEntity<User> responseEntity = new ResponseEntity<>(mockUser, HttpStatus.OK);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/users/" + userId), eq(HttpMethod.GET), eq(null),
				eq(User.class))).thenReturn(responseEntity);

		// Call the user service method
		User result = userService.getUserById(userId);

		// Assert the result
		assertEquals("Test User", result.getUsername());
	}

	@Test
	public void testGetNonExistentUserById() {
		// Prepare test data for a non-existent user ID
		Long userId = 100L;

		// Mock the HTTP request to the persistence microservice with a non-existent
		// user ID
		ResponseEntity<User> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

		when(restTemplate.exchange(eq(persistenceBaseUrl + "/users/" + userId), eq(HttpMethod.GET), eq(null),
				eq(User.class))).thenReturn(responseEntity);

		// Call the user service method and expect it to throw an appropriate exception
		assertThrows(Exception.class, () -> userService.getUserById(userId));
	}

	@Test
	public void testAddValidUser() {
		User newUser = new User(1L, "Test User", "test@example.com");

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.postForEntity(eq(persistenceBaseUrl + "/users"), eq(newUser), eq(User.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		// Call the user service method
		userService.addUser(newUser);

		// Verify that the restTemplate.postForEntity method was called with the correct
		// parameters
		verify(restTemplate, times(1)).postForEntity(eq(persistenceBaseUrl + "/users"), eq(newUser), eq(User.class));
	}

	@Test
	public void testAddUserBadRequest() {
		User newUser = new User(1L, "Test User", "test@example.com");

		// Mock the HTTP request to the persistence microservice with an error response
		// (Bad Request)
		when(restTemplate.postForEntity(eq(persistenceBaseUrl + "/users"), eq(newUser), eq(User.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

		// Call the user service method and expect it to throw an appropriate exception
		assertThrows(Exception.class, () -> userService.addUser(newUser));

		// Verify that the restTemplate.postForEntity method was called with the correct
		// parameters
		verify(restTemplate, times(1)).postForEntity(eq(persistenceBaseUrl + "/users"), eq(newUser), eq(User.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddUserWithEmptyUsername() {
		User newUser = new User(1L, "", "test@example.com");

		// Call the user service method and expect an IllegalArgumentException to be
		// thrown
		assertThrows(ValidationException.class, () -> userService.addUser(newUser));

		// Verify that the restTemplate.postForEntity method was not called
		verify(restTemplate, never()).postForEntity(any(), any(User.class), any(Class.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddUserWithEmptyEmail() {
		User newUser = new User(1L, "Test User", "");

		// Call the user service method and expect an IllegalArgumentException to be
		// thrown
		assertThrows(ValidationException.class, () -> userService.addUser(newUser));

		// Verify that the restTemplate.postForEntity method was not called
		verify(restTemplate, never()).postForEntity(any(), any(User.class), any(Class.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddUserWithInvalidEmail() {
		User newUser = new User(1L, "Test User", "invalid_email");

		// Call the user service method and expect an IllegalArgumentException to be
		// thrown
		assertThrows(ValidationException.class, () -> userService.addUser(newUser));

		// Verify that the restTemplate.postForEntity method was not called
		verify(restTemplate, never()).postForEntity(any(), any(User.class), any(Class.class));
	}
}
