package com.prgtech.javaslp.service;

import com.prgtech.javaslp.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JavaSlpService {
    private final List<Book> books = new ArrayList<>();
    private final List<IssuedBook> issuedBooks = new ArrayList<>();
    private final Map<String, User> users = new HashMap<>();

    public boolean registerUser(String username, Role role) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, role, new Book[2]));
        return true;
    }

    public boolean deregisterUser(String username) {
        return users.remove(username) != null;
    }

    public void addBook(Book book) {
        for (Book b : books) {
            if (b.getName().equalsIgnoreCase(book.getName())) {
                b.setCountOfCopy(b.getCountOfCopy() + book.getCountOfCopy());
                return;
            }
        }
        books.add(book);
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book b : books) if (b.getCountOfCopy() > 0) available.add(b);
        return available;
    }

    public String requestBook(String username, String bookName) {
        User user = users.get(username);
        if (user == null) return "User not found";
        if (!canBorrowBook(user)) return "Cannot borrow more than 2 books";
        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(bookName) && book.getCountOfCopy() > 0) {
                book.setCountOfCopy(book.getCountOfCopy() - 1);
                addBookToUser(user, book);
                issuedBooks.add(new IssuedBook(book, user));
                return "Book issued successfully";
            }
        }
        return "Book not available";
    }

    public String returnBook(String username, String bookName) {
        User user = users.get(username);
        if (user == null) return "User not found";
        if (!hasBook(user, bookName)) return "You do not have this book";
        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(bookName)) {
                book.setCountOfCopy(book.getCountOfCopy() + 1);
                removeBookFromUser(user, bookName);
                issuedBooks.removeIf(ib -> ib.getBook().getName().equalsIgnoreCase(bookName)
                        && ib.getUser().getUsername().equalsIgnoreCase(username));
                return "Book returned successfully";
            }
        }
        return "Book not found in inventory";
    }

    public Map<String, Object> generateReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("availableBooks", getAvailableBooks());
        report.put("issuedBooks", issuedBooks);
        return report;
    }

    private boolean canBorrowBook(User user) {
        for (Book b : user.getUserBooks()) if (b == null) return true;
        return false;
    }

    private void addBookToUser(User user, Book book) {
        Book[] arr = user.getUserBooks();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) { arr[i] = book; return;}
        }
    }

    private boolean hasBook(User user, String bookName) {
        for (Book b : user.getUserBooks()) {
            if (b != null && b.getName().equalsIgnoreCase(bookName)) return true;
        }
        return false;
    }

    private void removeBookFromUser(User user, String bookName) {
        Book[] arr = user.getUserBooks();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null && arr[i].getName().equalsIgnoreCase(bookName)) {
                arr[i] = null; return;
            }
        }
    }
}
