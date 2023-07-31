package com.bookstore.domain.bookstoredomain.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.domain.bookstoredomain.model.Book;
import com.bookstore.domain.bookstoredomain.service.BookService;

@RestController
@RequestMapping("bookstore/api/books")
public class BookController {

	@Autowired
	private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book newBook) {
        Book createdBook = bookService.addBook(newBook);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
}
