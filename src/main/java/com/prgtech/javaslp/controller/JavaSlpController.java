package com.prgtech.javaslp.controller;

import com.prgtech.javaslp.model.Book;
import com.prgtech.javaslp.model.Role;
import com.prgtech.javaslp.model.User;
import com.prgtech.javaslp.model.IssuedBook;
import com.prgtech.javaslp.service.JavaSlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/javaslp")
public class JavaSlpController {

    private final JavaSlpService service;

    @Autowired
    public JavaSlpController(JavaSlpService service) {
        this.service = service;
    }

    @PostMapping("/users/register")
    public Map<String, String> registerUser(@RequestParam String username, @RequestParam String role) {
        boolean result = service.registerUser(username, Role.valueOf(role.toUpperCase()));
        return Collections.singletonMap("result", result ? "User registered" : "User already exists");
    }

    @DeleteMapping("/users/{username}")
    public Map<String, String> deregisterUser(@PathVariable String username) {
        boolean removed = service.deregisterUser(username);
        return Collections.singletonMap("result", removed ? "User deregistered" : "User not found");
    }

    @PostMapping("/books")
    public Map<String, String> addBook(@RequestBody Book book) {
        service.addBook(book);
        return Collections.singletonMap("result", "Book added or updated");
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return service.getAvailableBooks();
    }
    
    @GetMapping("/books/all")
    public List<Book> getAllBooksIncludingUnavailable() {
        return service.getAllBooks();
    }

    @PostMapping("/users/{username}/request/{bookName}")
    public Map<String, String> borrowBook(@PathVariable String username, @PathVariable String bookName) {
        String result = service.requestBook(username, bookName);
        return Collections.singletonMap("result", result);
    }

    @PostMapping("/users/{username}/return/{bookName}")
    public Map<String, String> returnBook(@PathVariable String username, @PathVariable String bookName) {
        String result = service.returnBook(username, bookName);
        return Collections.singletonMap("result", result);
    }

    @GetMapping("/admin/report")
    public Map<String, Object> report() {
        return service.generateReport();
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
    
    @GetMapping("/users/{username}/books")
    public List<IssuedBook> getUserIssuedBooks(@PathVariable String username) {
        return service.getUserIssuedBooks(username);
    }

    // Debug endpoint
    @GetMapping("/ping")
    public String ping() { return "pong"; }
}
