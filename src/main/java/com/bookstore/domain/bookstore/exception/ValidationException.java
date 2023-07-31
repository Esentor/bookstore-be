package com.bookstore.domain.bookstore.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
        super(message);
    }
}
