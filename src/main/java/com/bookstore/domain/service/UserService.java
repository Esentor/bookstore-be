package com.bookstore.domain.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.model.User;
import com.bookstore.domain.validators.UserValidator;

@Service
public class UserService {

	private final RestTemplate restTemplate;
	@Value("${persistence.base.url}")
	private String persistenceBaseUrl;

	public UserService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<User> getAllUsers() {
		String url = persistenceBaseUrl + "/users";
		ResponseEntity<List<User>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<User>>() {
				});
		return response.getBody();
	}

	public User getUserById(Long userId) {
		String url = persistenceBaseUrl + "/users/" + userId;
		ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
		return response.getBody();
	}

	public void addUser(User newUser) {
		UserValidator.validateUser(newUser);

		String url = persistenceBaseUrl + "/users";
		restTemplate.postForEntity(url, newUser, User.class);
	}

}
