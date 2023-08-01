package com.bookstore.domain.validators;

import java.util.regex.Pattern;

import com.bookstore.domain.exception.ValidationException;
import com.bookstore.domain.model.User;

public class UserValidator {

	public static void validateUser(User user) {
		if (user == null) {
			throw new ValidationException("User cannot be null");
		}

		if (isEmpty(user.getUsername())) {
			throw new ValidationException("Username must not be empty");
		}

		if (isEmpty(user.getEmail())) {
			throw new ValidationException("Email must not be empty");
		}

		if (!isValidEmail(user.getEmail())) {
			throw new ValidationException("Email must be a valid email address");
		}
	}

	private static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private static boolean isValidEmail(String email) {
		String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		return Pattern.matches(emailPattern, email);
	}
}
