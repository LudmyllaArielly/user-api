package com.github.ludmylla.userapi.domain.repository;

import com.github.ludmylla.userapi.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
