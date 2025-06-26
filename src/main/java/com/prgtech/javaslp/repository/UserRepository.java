package com.prgtech.javaslp.repository;

import com.prgtech.javaslp.model.User;
import com.prgtech.javaslp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameIgnoreCase(String username);
    
    List<User> findByRole(Role role);
    
    boolean existsByUsernameIgnoreCase(String username);
} 