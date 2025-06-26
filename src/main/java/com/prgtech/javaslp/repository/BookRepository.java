package com.prgtech.javaslp.repository;

import com.prgtech.javaslp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByNameIgnoreCase(String name);
    
    @Query("SELECT b FROM Book b WHERE b.countOfCopy > 0")
    List<Book> findAvailableBooks();
    
    List<Book> findByCategory(String category);
    
    List<Book> findByAuthor(String author);
} 