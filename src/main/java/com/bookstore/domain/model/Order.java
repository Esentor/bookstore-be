package com.bookstore.domain.model;

import java.util.List;

public class Order {
	private Long id;
	private Long userId;
	private List<Book> books;
	private double totalAmount;

	public Order() {
		super();
	}
	
	public Order(Long id, Long userId, List<Book> books, double totalAmount) {
		super();
		this.id = id;
		this.userId = userId;
		this.books = books;
		this.totalAmount = totalAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

}
