package com.prgtech.javaslp.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssuedBook {
    private Book book;
    private User user;
}
