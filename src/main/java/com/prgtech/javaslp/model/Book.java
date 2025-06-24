package com.prgtech.javaslp.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String author;
    private String name;
    private String category;
    private int countOfCopy;
}
