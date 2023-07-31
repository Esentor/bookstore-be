package com.bookstore.domain.bookstore.service;

import static com.bookstore.domain.bookstore.common.Constants.PERSISTENCE_BASE_URL;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstore.exception.ValidationException;
import com.bookstore.domain.bookstore.model.Book;

@Service
public class BookService {

	private RestTemplate restTemplate;
	private final String persistenceBaseUrl = PERSISTENCE_BASE_URL;

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
        validateBook(newBook); // Validate the book before making the request

        String url = persistenceBaseUrl + "/books";
        ResponseEntity<Book> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(newBook), Book.class);
        return response.getBody();
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new ValidationException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new ValidationException("Book title must not be empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new ValidationException("Book author must not be empty");
        }

        if (book.getPrice() <= 0) {
            throw new ValidationException("Book price must be a positive value greater than 0");
        }
    }

}
