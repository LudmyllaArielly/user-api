package com.github.ludmylla.userapi.domain.service;

import com.github.ludmylla.userapi.domain.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(Long id);
}
