package com.bookstore.domain.bookstoredomain.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class User {
	
	private Long id;
	
	@NotEmpty(message = "Username must not be empty")
	private String username;

	@NotEmpty(message = "Email must not be empty")
	@Email(message = "Email must be a valid email address")
	private String email;

	public User() {
		super();
	}

	public User(Long id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
