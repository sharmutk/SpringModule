package com.prgtech.javaslp.service;

import com.prgtech.javaslp.model.*;
import com.prgtech.javaslp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class JavaSlpService {
    
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IssuedBookRepository issuedBookRepository;

    @Autowired
    public JavaSlpService(BookRepository bookRepository, UserRepository userRepository, 
                         IssuedBookRepository issuedBookRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    public boolean registerUser(String username, Role role) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return false;
        }
        User user = new User(username, role);
        userRepository.save(user);
        return true;
    }

    public boolean deregisterUser(String username) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Return all books before deregistering
            List<IssuedBook> userIssuedBooks = issuedBookRepository.findByUser(user);
            for (IssuedBook issuedBook : userIssuedBooks) {
                if (issuedBook.getReturnDate() == null) {
                    returnBookInternal(user, issuedBook.getBook());
                }
            }
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public void addBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByNameIgnoreCase(book.getName());
        if (existingBook.isPresent()) {
            Book existing = existingBook.get();
            existing.setCountOfCopy(existing.getCountOfCopy() + book.getCountOfCopy());
            bookRepository.save(existing);
        } else {
            bookRepository.save(book);
        }
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    public String requestBook(String username, String bookName) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        
        User user = userOpt.get();
        
        // Check if user can borrow more books (limit of 2)
        long currentlyIssuedCount = issuedBookRepository.countCurrentlyIssuedBooksByUser(user);
        if (currentlyIssuedCount >= 2) {
            return "Cannot borrow more than 2 books";
        }
        
        Optional<Book> bookOpt = bookRepository.findByNameIgnoreCase(bookName);
        if (bookOpt.isEmpty()) {
            return "Book not found";
        }
        
        Book book = bookOpt.get();
        if (book.getCountOfCopy() <= 0) {
            return "Book not available";
        }
        
        // Check if user already has this book
        Optional<IssuedBook> existingIssue = issuedBookRepository.findByUserAndBookAndReturnDateIsNull(user, book);
        if (existingIssue.isPresent()) {
            return "You already have this book";
        }
        
        // Issue the book
        book.setCountOfCopy(book.getCountOfCopy() - 1);
        bookRepository.save(book);
        
        IssuedBook issuedBook = new IssuedBook(book, user);
        issuedBookRepository.save(issuedBook);
        
        return "Book issued successfully";
    }

    public String returnBook(String username, String bookName) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        
        User user = userOpt.get();
        
        Optional<Book> bookOpt = bookRepository.findByNameIgnoreCase(bookName);
        if (bookOpt.isEmpty()) {
            return "Book not found";
        }
        
        Book book = bookOpt.get();
        
        Optional<IssuedBook> issuedBookOpt = issuedBookRepository.findByUserAndBookAndReturnDateIsNull(user, book);
        if (issuedBookOpt.isEmpty()) {
            return "You do not have this book";
        }
        
        return returnBookInternal(user, book);
    }
    
    private String returnBookInternal(User user, Book book) {
        Optional<IssuedBook> issuedBookOpt = issuedBookRepository.findByUserAndBookAndReturnDateIsNull(user, book);
        if (issuedBookOpt.isPresent()) {
            // Mark book as returned
            IssuedBook issuedBook = issuedBookOpt.get();
            issuedBook.setReturnDate(LocalDateTime.now());
            issuedBookRepository.save(issuedBook);
            
            // Increase book count
            book.setCountOfCopy(book.getCountOfCopy() + 1);
            bookRepository.save(book);
            
            return "Book returned successfully";
        }
        return "Book not found in your issued books";
    }

    public Map<String, Object> generateReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("availableBooks", getAvailableBooks());
        report.put("currentlyIssuedBooks", issuedBookRepository.findCurrentlyIssuedBooks());
        report.put("allBooks", bookRepository.findAll());
        report.put("allUsers", userRepository.findAll());
        return report;
    }
    
    // Additional methods for better functionality
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public List<IssuedBook> getUserIssuedBooks(String username) {
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
        if (userOpt.isPresent()) {
            return issuedBookRepository.findByUser(userOpt.get());
        }
        return new ArrayList<>();
    }
}
