package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.repository.UserRepository;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.domain.service.exceptions.BusinessException;
import com.github.ludmylla.userapi.domain.service.exceptions.RoleNotFoundException;
import com.github.ludmylla.userapi.domain.service.exceptions.UserNotFoundException;
import com.github.ludmylla.userapi.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleServiceImpl roleService;

    @Transactional
    @Override
    public User create(User user) {
        try {
            validationUser(user);
            return userRepository.save(user);
        } catch (RoleNotFoundException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User update(Long id, User user) {
        try {
            User userActual = findById(id);
            user.setId(userActual.getId());
            validationUser(user);
            return userRepository.save(user);

        } catch (RoleNotFoundException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    private void validationUser(User user) {
        findByEmailUsed(user);
        verifyIfUserRoleExits(user);
        encryptPassword(user);
    }

    private Optional<User> findByEmailUsed(User user) {
        Optional<User> userEmail = userRepository.findByEmailOptional(user.getEmail());
        System.out.println(userEmail);
        if (userEmail.isPresent() && !userEmail.get().getId().equals(user.getId())) {
            throw new BusinessException(Messages.MSG_EMAIL_IN_USE);
        }
        return userEmail;
    }

    private void verifyIfUserRoleExits(User user) {
        Set<Role> roles = new HashSet<>();

        for (Role role : user.getRoles()) {

            Role roleFindById = roleService.findById(role.getId());

            if (roleFindById == null) {
                throw new RoleNotFoundException("Role not exist.");
            }
            roles.add(roleFindById);
            user.setRoles(roles);
        }
    }

    private void encryptPassword(User user) {
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
