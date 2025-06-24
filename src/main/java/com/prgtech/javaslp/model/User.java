package com.prgtech.javaslp.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private Role role;
    private Book[] userBooks = new Book[2];
}
