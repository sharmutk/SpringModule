package com.prgtech.javaslp.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issued_books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime issuedDate;
    
    private LocalDateTime returnDate;
    
    // Constructor without ID for creating new issued books
    public IssuedBook(Book book, User user) {
        this.book = book;
        this.user = user;
        this.issuedDate = LocalDateTime.now();
    }
}
