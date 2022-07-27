package com.github.ludmylla.userapi.domain.service;

import com.github.ludmylla.userapi.domain.dto.UserDTO;
import com.github.ludmylla.userapi.domain.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User create(UserDTO dto);

    User update(UserDTO dto);

    void delete(Long id);
}
