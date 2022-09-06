package com.github.ludmylla.userapi.api.controller;

import com.github.ludmylla.userapi.api.assembler.UserModelAssembler;
import com.github.ludmylla.userapi.api.disassembler.UserInputDisassembler;
import com.github.ludmylla.userapi.domain.dto.UserModel;
import com.github.ludmylla.userapi.domain.dto.input.RoleIdInput;
import com.github.ludmylla.userapi.domain.dto.input.UserInput;
import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;


class UserControllerTest {

    public static final long ID = 1L;
    public static final String NAME = "Ana";
    public static final String EMAIL = "ana@xyz.com";
    public static final String PASSWORD = "123";
    public static final int INDEX = 0;

    @Mock
    ModelMapper mapper;

    @Mock
    UserService userService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    UserModelAssembler userModelAssembler;
    @Mock
    UserInputDisassembler userInputDisassembler;
    @InjectMocks
    UserController userController;

    UserModel userModel;
    UserInput userInput;
    User user;
    Role role;
    RoleIdInput rolesId;
    Set<RoleIdInput> roleIdInput = new HashSet<>();
    Set<Role> roles = new HashSet<>();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prepareData();
    }

    @Test
    void shouldReturnSuccess_WhenToFindByIdUser() {
        Mockito.when(userService.findById(anyLong())).thenReturn(user);

        ResponseEntity<UserModel> response = userController.findById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());

    }

    @Test
    void shouldReturnSuccess_WhenToListAllUsers() {
        Mockito.when(userService.findAll()).thenReturn(List.of(user));

        ResponseEntity<List<UserModel>> response = userController.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(LinkedList.class, response.getBody().getClass());
    }

    @Test
    void shouldReturnCreated_WhenToCreateUser() {
        Mockito.when(userService.create(any())).thenReturn(user);

        ResponseEntity<UserModel> response = userController.create(userInput);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1))
                .create(any());
    }

    @Test
    void shouldReturnSuccess_WhenToUpdateUser(){
        Mockito.when(userService.update(anyLong(), any())).thenReturn(user);

        ResponseEntity<UserModel> response = userController.update(ID, userInput);

        assertNotNull(response);
        assertNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

        Mockito.verify(userService, Mockito.times(1))
                .update(anyLong(), any());
    }

    @Test
    void shouldReturnSuccess_WhenToDeleteUser(){
        Mockito.doNothing().when(userService).delete(anyLong());

        ResponseEntity<UserModel> response = userController.delete(ID);

        assertNotNull(response);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

        Mockito.verify(userService, Mockito.times(1))
                .delete(anyLong());
    }

    private void prepareData(){
        role = new Role(1L, "ROLE_USER");
        roles.add(role);

        rolesId = new RoleIdInput(1L);
        roleIdInput.add(rolesId);

        user = new User(ID, NAME, EMAIL, PASSWORD, roles);
        userInput = new UserInput(NAME, EMAIL, PASSWORD, roleIdInput);
    }
}
