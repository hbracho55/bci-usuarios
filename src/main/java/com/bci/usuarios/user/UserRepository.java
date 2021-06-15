package com.bci.usuarios.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.token=?2")
    Optional<User> findByIdAndToken(String userId, String token);

    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.password=?2")
    Optional<User> findByEmailAndPassword(String email, String password);
}
