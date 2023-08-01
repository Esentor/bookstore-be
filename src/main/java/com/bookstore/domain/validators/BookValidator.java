package com.bookstore.domain.validators;

import com.bookstore.domain.exception.ValidationException;
import com.bookstore.domain.model.Book;

public class BookValidator {

    public static void validateBook(Book book) {
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
