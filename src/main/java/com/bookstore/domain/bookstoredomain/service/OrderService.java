package com.bookstore.domain.bookstoredomain.service;

import static com.bookstore.domain.bookstoredomain.common.Constants.PERSISTENCE_BASE_URL;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstoredomain.model.Book;
import com.bookstore.domain.bookstoredomain.model.Order;

@Service
public class OrderService {

	private final RestTemplate restTemplate;
    private final String persistenceBaseUrl = PERSISTENCE_BASE_URL;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Order createOrder(Long userId, List<Book> selectedBooks) {
        String url = persistenceBaseUrl + "/orders/" + userId;
        ResponseEntity<Order> response = restTemplate.postForEntity(url, selectedBooks, Order.class);
        return response.getBody();
    }

    public List<Order> getUserOrders(Long userId) {
        String url = persistenceBaseUrl + "/orders/user/" + userId;
        ResponseEntity<List<Order>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {});
        return response.getBody();
    }
}
