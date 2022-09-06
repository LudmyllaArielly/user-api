package com.github.ludmylla.userapi.domain.service.exceptions;

import com.github.ludmylla.userapi.api.exceptionHandler.ApiExceptionHandler;
import com.github.ludmylla.userapi.api.exceptionHandler.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ResourceExceptionHandleTest {

    public static final String USER_NOT_FOUND = "User not found.";
    public static final String EMAIL_IN_USE = "Email is in use";
    public static final String BAD_CREDENTIALS = "Authentication failed. Username or password not valid.";
    public static final String ROLE_NOT_FOUND = "Role not exist.";

    @Mock
    private WebRequest request;

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenEntityNotFoundExceptionThenReturnAResponseEntity() {
        ResponseEntity<?> response = apiExceptionHandler
                .handleEntityNotFoundException(
                        new EntityNotFoundException(USER_NOT_FOUND),
                        request);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Problem.class, response.getBody().getClass());
    }

    @Test
    void whenBusinessExceptionThenReturnAResponseEntity() {
        ResponseEntity<?> response = apiExceptionHandler
                .handleBusinessException(
                        new BusinessException(EMAIL_IN_USE),
                        request);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Problem.class, response.getBody().getClass());
    }

    @Test
    void whenUserBadCredentialsExceptionThenReturnAResponseEntity() {
        ResponseEntity<?> response = apiExceptionHandler
                .handleBadCredentialsException(
                        new UserBadCredentialsException(BAD_CREDENTIALS),
                        request);

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Problem.class, response.getBody().getClass());
    }
}
