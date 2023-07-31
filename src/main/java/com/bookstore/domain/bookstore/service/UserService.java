package com.bookstore.domain.bookstore.service;

import static com.bookstore.domain.bookstore.common.Constants.PERSISTENCE_BASE_URL;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstore.exception.ValidationException;
import com.bookstore.domain.bookstore.model.User;

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
        if (!isValidUser(newUser)) {
            throw new ValidationException("Invalid user data");
        }

        String url = persistenceBaseUrl + "/users";
        restTemplate.postForEntity(url, newUser, User.class);
    }

    private boolean isValidUser(User user) {
        // Check for non-empty username and email
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }

        // Check if the email is a valid email address using a simple regex pattern
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailPattern, user.getEmail());
    }
}
