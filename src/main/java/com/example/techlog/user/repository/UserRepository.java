package com.example.techlog.user.repository;

import com.example.techlog.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.isDeleted = false and u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String Email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> getUserWithTag(@Param("email") String email);

    @Query("SELECT DISTINCT pt.tag.id FROM User u JOIN u.posts p JOIN p.postTags pt " +
            "WHERE u.id = :userId AND p.id <> :excludedPostId AND pt.tag.id IN :tagIds")
    List<Long> findRemainingTags(@Param("userId") Long userId,
                                 @Param("excludedPostId") Long excludedPostId,
                                 @Param("tagIds") List<Long> tagIds);
}
