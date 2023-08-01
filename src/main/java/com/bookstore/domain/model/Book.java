package com.bookstore.domain.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class Book {
	
	private Long id;
	
    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    private String author;

    @Positive(message = "Price must be a positive value greater than 0")
    private double price;

	public Book() {
		super();
	}

	public Book(Long id, String title, String author, double price) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
