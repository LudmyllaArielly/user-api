package com.github.ludmylla.userapi.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel {

    private Long id;
    private String name;
    private String email;
    private List<RoleModel> roles;

}
