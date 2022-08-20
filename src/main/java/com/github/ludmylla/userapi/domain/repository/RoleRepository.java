package com.github.ludmylla.userapi.domain.repository;

import com.github.ludmylla.userapi.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{

   Role findByRoleName(String roleName);
}
