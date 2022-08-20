package com.github.ludmylla.userapi.domain.service;

import com.github.ludmylla.userapi.domain.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findAll();

    User update(Long id, User user);

    void delete(Long id);
}
