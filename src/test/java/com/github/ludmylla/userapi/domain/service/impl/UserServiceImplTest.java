package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.dto.UserDTO;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.repository.UserRepository;
import com.github.ludmylla.userapi.domain.service.exceptions.DataIntegrityViolationException;
import com.github.ludmylla.userapi.domain.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

class UserServiceImplTest {

    public static final Long ID = 1L;
    public static final String NAME = "Maria";
    public static final String EMAIL = "maria@xyz.com";
    public static final String PASSWORD = "123";
    public static final int INDEX = 0;
    public static final String OBJECT_NOT_FOUND = "Object not found.";
    public static final String EMAIL_IN_USE = "Email in use";


    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private User user;
    private UserDTO dto;
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
    void shouldReturnObjectNotFoundException_WhenToConsultUserWithIdThatDoesNotExist() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            userServiceImpl.findById(ID);

        }catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJECT_NOT_FOUND, ex.getMessage());
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

        User response = userServiceImpl.create(dto);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void shouldReturnDataIntegrityViolationException_WhenToCreateAUserWithEmailInUser() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(userOptional);

        try {
            userOptional.get().setId(2L);
            userServiceImpl.create(dto);

        }catch (Exception ex) {
            assertEquals(DataIntegrityViolationException.class, ex.getClass());
            assertEquals(EMAIL_IN_USE, ex.getMessage());
        }
    }

    @Test
    void shouldReturnSuccess_WhenUpdateUser() {
        Mockito.when(userRepository.save(any())).thenReturn(user);

        User response = userServiceImpl.update(dto);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void shouldReturnDataIntegrityViolationException_WhenToUpdateAUserWithEmailInUser() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(userOptional);

        try {
            userOptional.get().setId(2L);
            userServiceImpl.update(dto);

        }catch (Exception ex) {
            assertEquals(DataIntegrityViolationException.class, ex.getClass());
            assertEquals(EMAIL_IN_USE, ex.getMessage());
        }

    }

    @Test
    void testDelete() {
    }

    private void prepareData() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        dto = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        userOptional = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}


