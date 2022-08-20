package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.repository.UserRepository;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.domain.service.exceptions.DataIntegrityViolationException;
import com.github.ludmylla.userapi.domain.service.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public User create(User user) {
        validationUser(user);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Object not found."));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User user) {
        User userActual = findById(id);
        user.setId(userActual.getId());
        validationUser(user);
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    private void validationUser(User user){
        findByEmailUsed(user);
        verifyIfUserRoleExits(user);
        encryptPassword(user);
    }

    private void findByEmailUsed(User user) {
        User userEmail = userRepository.findByEmail(user.getEmail());

        if (userEmail != null && !userEmail.getEmail().equals(user.getEmail())) {
            throw new DataIntegrityViolationException("Email in use");
        }
    }

    private void verifyIfUserRoleExits(User user){
        Set<Role> roles = new HashSet<>();

        for(Role role: user.getRoles()){

            Role roleFindById = roleService.findById(role.getId());

            if(roleFindById == null){
                throw new RoleNotFoundException("Role not exist.");
            }
            roles.add(roleFindById);
            user.setRoles(roles);
        }
    }

    private void encryptPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

}
