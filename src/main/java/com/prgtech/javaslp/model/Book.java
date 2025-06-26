package com.prgtech.javaslp.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int countOfCopy;

    // Constructor without ID for creating new books
    public Book(String author, String name, String category, int countOfCopy) {
        this.author = author;
        this.name = name;
        this.category = category;
        this.countOfCopy = countOfCopy;
    }
}
