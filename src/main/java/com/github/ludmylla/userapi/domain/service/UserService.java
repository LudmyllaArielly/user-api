package com.github.ludmylla.userapi.domain.service;

import com.github.ludmylla.userapi.domain.dto.AuthToken;
import com.github.ludmylla.userapi.domain.dto.LoginDTO;
import com.github.ludmylla.userapi.domain.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    AuthToken login(LoginDTO loginDTO);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findAll();

    User update(Long id, User user);

    void delete(Long id);
}
