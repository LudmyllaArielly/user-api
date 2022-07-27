package com.github.ludmylla.userapi.domain.service;

import com.github.ludmylla.userapi.domain.model.User;

public interface UserService {

    User findById(Long id);

}
