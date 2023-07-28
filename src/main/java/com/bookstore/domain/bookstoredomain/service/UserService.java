package com.bookstore.domain.bookstoredomain.service;

import static com.bookstore.domain.bookstoredomain.common.Constants.PERSISTENCE_BASE_URL;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstoredomain.model.User;

@Service
public class UserService {

	private final RestTemplate restTemplate;
    private final String persistenceBaseUrl = PERSISTENCE_BASE_URL;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        String url = persistenceBaseUrl + "/users";
        ResponseEntity<List<User>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        return response.getBody();
    }

    public User getUserById(Long userId) {
        String url = persistenceBaseUrl + "/users/" + userId;
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
        return response.getBody();
    }

    public void addUser(User newUser) {
        String url = persistenceBaseUrl + "/users";
        restTemplate.postForEntity(url, newUser, User.class);
    }
}
