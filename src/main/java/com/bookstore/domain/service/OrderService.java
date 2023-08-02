package com.bookstore.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.model.Book;
import com.bookstore.domain.model.Order;

@Service
public class OrderService {

	private final RestTemplate restTemplate;
	@Value("${persistence.base.url}")
	private String persistenceBaseUrl;

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
		ResponseEntity<List<Order>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Order>>() {
				});
		return response.getBody();
	}
}
