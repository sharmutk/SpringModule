package com.prgtech.javaslp.config;

import com.prgtech.javaslp.model.Book;
import com.prgtech.javaslp.model.Role;
import com.prgtech.javaslp.service.JavaSlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JavaSlpService javaSlpService;

    @Autowired
    public DataInitializer(JavaSlpService javaSlpService) {
        this.javaSlpService = javaSlpService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize some sample books
        javaSlpService.addBook(new Book("Robert C. Martin", "Clean Code", "Programming", 3));
        javaSlpService.addBook(new Book("Joshua Bloch", "Effective Java", "Programming", 2));
        javaSlpService.addBook(new Book("Gang of Four", "Design Patterns", "Programming", 1));
        javaSlpService.addBook(new Book("Martin Fowler", "Refactoring", "Programming", 2));
        javaSlpService.addBook(new Book("Eric Evans", "Domain-Driven Design", "Architecture", 1));
        
        // Initialize some sample users
        javaSlpService.registerUser("john_student", Role.STUDENT);
        javaSlpService.registerUser("jane_prof", Role.PROFESSOR);
        javaSlpService.registerUser("admin_user", Role.ADMIN);
        javaSlpService.registerUser("lib_staff", Role.LIBRARIAN);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("- Books added: 5");
        System.out.println("- Users registered: 4");
        System.out.println("- Access H2 Console at: http://localhost:8080/h2-console");
        System.out.println("- JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("- Username: sa (no password)");
    }
} 