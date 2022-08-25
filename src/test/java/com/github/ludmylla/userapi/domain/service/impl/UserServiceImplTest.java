package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.repository.RoleRepository;
import com.github.ludmylla.userapi.domain.repository.UserRepository;
import com.github.ludmylla.userapi.domain.service.exceptions.BusinessException;
import com.github.ludmylla.userapi.domain.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class UserServiceImplTest {

    public static final Long ID = 1L;
    public static final String NAME = "Maria";
    public static final String EMAIL = "maria@xyz.com";
    public static final String PASSWORD = "123";
    public static final int INDEX = 0;
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String EMAIL_IN_USE = "Email in use";

    @Mock(name = "role")
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private Set<Role> roles = new HashSet<>();
    private User user;
    private Role role;
    private Optional<Role> roleOptional;
    private Optional<User> userOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prepareData();
    }

    @Test
    void shouldReturnSuccess_WhenToConsultUser() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(userOptional);

        User response = userServiceImpl.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void shouldReturnUserNotFoundException_WhenToConsultUserWithIdThatDoesNotExist() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException(USER_NOT_FOUND));

        try {
            userServiceImpl.findById(ID);

        }catch (Exception ex) {
            assertEquals(UserNotFoundException.class, ex.getClass());
            assertEquals(USER_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    void shouldReturnSuccess_WhenToListUsers(){
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> response = userServiceImpl.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());
    }

    @Test
    void shouldReturnSuccess_WhenToCreateUser() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(roleRepository.findById(1L)).thenReturn(roleOptional);

        Role responseRole = roleService.findById(1L);
        User response = userServiceImpl.create(user);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(passwordEncoder.encode(PASSWORD), response.getPassword());
    }

    @Test
    void shouldReturnBusinessException_WhenToCreateAUserWithEmailInUser() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(user);

        try {
            userOptional.get().setId(2L);
            userServiceImpl.create(user);

        }catch (Exception ex) {
            assertEquals(BusinessException.class, ex.getClass());
            assertEquals(EMAIL_IN_USE, ex.getMessage());
        }
    }

    @Test
    void shouldReturnSuccess_WhenUpdateToUser() {
        Mockito.when(userRepository.save(any())).thenReturn(user);

        User response = userServiceImpl.update(ID, user);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void shouldReturnBusinessException_WhenToUpdateAUserWithEmailInUser() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(user);

        try {
            userOptional.get().setId(2L);
            userServiceImpl.update(ID, user);

        }catch (Exception ex) {
            assertEquals(BusinessException.class, ex.getClass());
            assertEquals(EMAIL_IN_USE, ex.getMessage());
        }
    }

    @Test
    void shouldReturnSuccess_WhenToDeleteUser() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(userOptional);

        Mockito.doNothing().when(userRepository).deleteById(anyLong());
        userServiceImpl.delete(ID);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void shouldReturnObjectNotFoundException_WhenToDeleteUserWithIdThatDoesNotExist() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException(USER_NOT_FOUND));

        try {
            userServiceImpl.delete(ID);
        }catch (Exception ex) {
            assertEquals(UserNotFoundException.class, ex.getClass());
            assertEquals(USER_NOT_FOUND, ex.getMessage());
        }
    }


    private void prepareData() {

        role = new Role(1L, "ROLE_USER");
        roles.add(role);

        user = new User(ID, NAME, EMAIL, PASSWORD, roles);

        userOptional = Optional.of(new User(ID, NAME, EMAIL, PASSWORD, roles));
        roleOptional = Optional.of(new Role(1L, "ROLE_USER"));
    }
}


