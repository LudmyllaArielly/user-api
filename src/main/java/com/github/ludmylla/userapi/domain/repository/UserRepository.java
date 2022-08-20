package com.github.ludmylla.userapi.domain.repository;

import com.github.ludmylla.userapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("from User u where u.email = :email")
    Optional<User> findByEmailOptional(@Param("email") String email);

}
