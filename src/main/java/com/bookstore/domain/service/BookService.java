package com.bookstore.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.model.Book;
import com.bookstore.domain.validators.BookValidator;

@Service
public class BookService {

	private RestTemplate restTemplate;
	@Value("${persistence.base.url}")
	private String persistenceBaseUrl;

	public BookService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Book> getAllBooks() {
		String url = persistenceBaseUrl + "/books";
		ResponseEntity<List<Book>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Book>>() {
				});
		return response.getBody();
	}

	public Book getBookById(Long bookId) {
		String url = persistenceBaseUrl + "/books/" + bookId;
		ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.GET, null, Book.class);
		return response.getBody();
	}

	public Book addBook(Book newBook) {
		BookValidator.validateBook(newBook); // Validate the book before making the request

		String url = persistenceBaseUrl + "/books";
		ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(newBook),
				Book.class);
		return response.getBody();
	}
}
