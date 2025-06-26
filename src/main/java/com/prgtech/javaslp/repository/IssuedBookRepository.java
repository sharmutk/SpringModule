package com.prgtech.javaslp.repository;

import com.prgtech.javaslp.model.IssuedBook;
import com.prgtech.javaslp.model.User;
import com.prgtech.javaslp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {
    
    List<IssuedBook> findByUser(User user);
    
    List<IssuedBook> findByBook(Book book);
    
    @Query("SELECT ib FROM IssuedBook ib WHERE ib.returnDate IS NULL")
    List<IssuedBook> findCurrentlyIssuedBooks();
    
    Optional<IssuedBook> findByUserAndBookAndReturnDateIsNull(User user, Book book);
    
    @Query("SELECT COUNT(ib) FROM IssuedBook ib WHERE ib.user = :user AND ib.returnDate IS NULL")
    long countCurrentlyIssuedBooksByUser(User user);
} 