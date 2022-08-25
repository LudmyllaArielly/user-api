package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.repository.RoleRepository;
import com.github.ludmylla.userapi.domain.service.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        return role;
    }

    public Role findByRoleName(String roleName){
        return roleRepository.findByRoleName(roleName);
    }

}
