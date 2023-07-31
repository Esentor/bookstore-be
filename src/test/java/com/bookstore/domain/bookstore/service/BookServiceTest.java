package com.bookstore.domain.bookstore.service;

import static com.bookstore.domain.bookstore.common.Constants.PERSISTENCE_BASE_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bookstore.domain.bookstore.exception.ValidationException;
import com.bookstore.domain.bookstore.model.Book;
import com.bookstore.domain.bookstore.service.BookService;

public class BookServiceTest {

	private final String persistenceBaseUrl = PERSISTENCE_BASE_URL;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private BookService bookService;

	@BeforeEach
	public void setUp() {
		restTemplate = mock(RestTemplate.class);
		bookService = new BookService(restTemplate);
	}

	@Test
	public void testGetAllBooks() {
		// Prepare the mock response from the persistence microservice
		List<Book> mockBooks = new ArrayList<>();
		mockBooks.add(new Book(1L, "Book 1", "Author 1", 19.99));
		mockBooks.add(new Book(2L, "Book 2", "Author 2", 24.99));

		ResponseEntity<List<Book>> responseEntity = new ResponseEntity<>(mockBooks, HttpStatus.OK);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/books"), eq(HttpMethod.GET), any(),
				eq(new ParameterizedTypeReference<List<Book>>() {
				}))).thenReturn(responseEntity);

		// Call the book service method
		List<Book> result = bookService.getAllBooks();

		// Assert the result
		assertEquals(2, result.size());
		assertEquals("Book 1", result.get(0).getTitle());
		assertEquals("Book 2", result.get(1).getTitle());
	}

	@Test
	public void testGetBookById() {
		// Prepare the mock response from the persistence microservice
		Book mockBook = new Book(1L, "Book 1", "Author 1", 19.99);

		ResponseEntity<Book> responseEntity = new ResponseEntity<>(mockBook, HttpStatus.OK);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/books/1"), eq(HttpMethod.GET), any(), eq(Book.class)))
				.thenReturn(responseEntity);

		// Call the book service method
		Book result = bookService.getBookById(1L);

		// Assert the result
		assertNotNull(result);
		assertEquals("Book 1", result.getTitle());
		assertEquals("Author 1", result.getAuthor());
	}

	@Test
	public void testAddValidBook() {
		Book newBook = new Book(null, "New Book", "New Author", 29.99);

		// Prepare the mock response from the persistence microservice
		Book createdBook = new Book(1L, "New Book", "New Author", 29.99);
		ResponseEntity<Book> responseEntity = new ResponseEntity<>(createdBook, HttpStatus.CREATED);

		// Mock the HTTP request to the persistence microservice
		when(restTemplate.exchange(eq(persistenceBaseUrl + "/books"), eq(HttpMethod.POST), any(HttpEntity.class),
				eq(Book.class))).thenReturn(responseEntity);

		// Call the book service method
		Book result = bookService.addBook(newBook);

		// Assert the result
		assertEquals(createdBook, result);

		// Verify that the restTemplate.exchange method was called with the correct
		// parameters
		verify(restTemplate, times(1)).exchange(eq(persistenceBaseUrl + "/books"), eq(HttpMethod.POST),
				any(HttpEntity.class), eq(Book.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddBookWithEmptyTitle() {
		// Test with a book having an empty title
		Book newBook = new Book(null, "", "Author", 19.99);

		assertThrows(ValidationException.class, () -> bookService.addBook(newBook));
		verify(restTemplate, never()).exchange(any(), any(HttpMethod.class), any(), any(Class.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddBookWithEmptyAuthor() {
		// Test with a book having an empty author
		Book newBook = new Book(null, "Title", "", 19.99);

		assertThrows(ValidationException.class, () -> bookService.addBook(newBook));
		verify(restTemplate, never()).exchange(any(), any(HttpMethod.class), any(), any(Class.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddBookWithInvalidPrice() {
		// Test with a book having an invalid price (negative value)
		Book newBook = new Book(null, "Title", "Author", -5.99);

		assertThrows(ValidationException.class, () -> bookService.addBook(newBook));
		verify(restTemplate, never()).exchange(any(), any(HttpMethod.class), any(), any(Class.class));
	}

}
