package com.example.techlog.user.repository;

import com.example.techlog.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.isDeleted = false and u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String Email);

}
