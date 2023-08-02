package com.bookstore.domain.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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

import com.bookstore.domain.model.Book;
import com.bookstore.domain.model.Order;

public class OrderServiceTest {

	@Value("${persistence.base.url}")
	private String persistenceBaseUrl;

	private RestTemplate restTemplate;
	private OrderService orderService;

	@BeforeEach
	public void setUp() {
		restTemplate = mock(RestTemplate.class);
		orderService = new OrderService(restTemplate);
	}

	@Test
	public void testCreateOrder() {
		// Prepare test data
		Long userId = 1L;
		List<Book> selectedBooks = new ArrayList<>();
		selectedBooks.add(new Book(1L, "Book 1", "Author 1", 19.99));
		selectedBooks.add(new Book(2L, "Book 2", "Author 2", 24.99));

		// Prepare the mock response from the persistence microservice
		Order mockOrder = new Order();
		mockOrder.setId(12345L);
		mockOrder.setUserId(userId);
		mockOrder.setBooks(selectedBooks);

		ResponseEntity<Order> responseEntity = new ResponseEntity<>(mockOrder, HttpStatus.CREATED);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.postForEntity(eq(persistenceBaseUrl + "/orders/" + userId), eq(selectedBooks),
				eq(Order.class))).thenReturn(responseEntity);

		// Call the order service method
		Order result = orderService.createOrder(userId, selectedBooks);

		// Assert the result
		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertEquals(selectedBooks.size(), result.getBooks().size());
	}

	@Test
	public void testCreateOrderWithEmptyBooksList() {
		// Prepare test data
		Long userId = 1L;
		List<Book> selectedBooks = new ArrayList<>(); // Empty list

		// Mock the HTTP request to the persistence microservice with an empty list of
		// selected books
		ResponseEntity<Order> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		when(restTemplate.postForEntity(eq(persistenceBaseUrl + "/orders/" + userId), eq(selectedBooks),
				eq(Order.class))).thenReturn(responseEntity);

		// Call the order service method and expect it to throw an appropriate exception
		assertThrows(Exception.class, () -> orderService.createOrder(userId, selectedBooks));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetUserOrders() {
		// Prepare test data
		Long userId = 1L;

		// Prepare the mock response from the persistence microservice
		List<Order> mockOrders = new ArrayList<>();
		mockOrders.add(new Order(12345L, userId, new ArrayList<>(), 29.99));
		mockOrders.add(new Order(67890L, userId, new ArrayList<>(), 59.98));

		ResponseEntity<List<Order>> responseEntity = new ResponseEntity<>(mockOrders, HttpStatus.OK);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/orders/user/" + userId), eq(HttpMethod.GET), eq(null),
				any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

		// Call the order service method
		List<Order> result = orderService.getUserOrders(userId);

		// Assert the result
		assertNotNull(result);
		assertEquals(mockOrders.size(), result.size());
		assertEquals(userId, result.get(0).getUserId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetUserOrdersEmptyList() {
		// Prepare test data
		Long userId = 1L;

		// Mock the HTTP request to the persistence microservice with an empty list of
		// orders
		List<Order> mockOrders = new ArrayList<>();
		ResponseEntity<List<Order>> responseEntity = new ResponseEntity<>(mockOrders, HttpStatus.OK);

		when(restTemplate.exchange(eq(persistenceBaseUrl + "/orders/user/" + userId), eq(HttpMethod.GET), eq(null),
				any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

		// Call the order service method
		List<Order> result = orderService.getUserOrders(userId);

		// Assert the result
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetUserOrdersNonExistentUser() {
		// Prepare test data for a non-existent user ID
		Long userId = 100L;

		// Mock the HTTP request to the persistence microservice with a non-existent
		// user ID
		ResponseEntity<List<Order>> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

		when(restTemplate.exchange(eq(persistenceBaseUrl + "/orders/user/" + userId), eq(HttpMethod.GET), eq(null),
				any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

		// Call the order service method and expect it to throw an appropriate exception
		assertThrows(Exception.class, () -> orderService.getUserOrders(userId));
	}

}
