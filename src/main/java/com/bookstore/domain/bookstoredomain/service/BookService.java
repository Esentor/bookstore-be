package com.bookstore.domain.bookstoredomain.service;

import static com.bookstore.domain.bookstoredomain.common.Constants.PERSISTENCE_BASE_URL;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstoredomain.model.Book;

@Service
public class BookService {

	private final RestTemplate restTemplate;
    private final String persistenceBaseUrl = PERSISTENCE_BASE_URL;

    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> getAllBooks() {
        String url = persistenceBaseUrl + "/books";
        ResponseEntity<List<Book>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {});
        return response.getBody();
    }

    public Book getBookById(Long bookId) {
        String url = persistenceBaseUrl + "/books/" + bookId;
        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.GET, null, Book.class);
        return response.getBody();
    }

    public void addBook(Book newBook) {
        String url = persistenceBaseUrl + "/books";
        restTemplate.postForEntity(url, newBook, Book.class);
    }
}
