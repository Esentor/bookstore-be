package com.bookstore.domain.bookstoredomain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.domain.bookstoredomain.model.Book;
import com.bookstore.domain.bookstoredomain.model.Order;
import com.bookstore.domain.bookstoredomain.service.OrderService;

@RestController
@RequestMapping("bookstore/api/orders")
public class OrderController {
	
	@Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    public Order createOrder(@PathVariable Long userId, @RequestBody List<Book> selectedBooks) {
        return orderService.createOrder(userId, selectedBooks);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

}
