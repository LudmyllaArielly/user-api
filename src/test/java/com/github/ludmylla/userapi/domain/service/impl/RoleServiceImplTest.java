package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.model.Role;
import com.github.ludmylla.userapi.domain.repository.RoleRepository;
import com.github.ludmylla.userapi.domain.service.exceptions.RoleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RoleServiceImplTest {

    public static final Long ID = 1L;
    public static final String ROLE_NOT_FOUND = "Role not exist.";

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    private Optional<Role> roleOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prepareData();
    }

    @Test
    void shouldReturnSuccess_WhenToConsultRole() {
        Mockito.when(roleRepository.findById(Mockito.anyLong())).thenReturn(roleOptional);

        Role response = roleService.findById(ID);

        assertNotNull(response);
        assertEquals(Role.class, response.getClass());
        assertEquals(ID, response.getId());
    }

    @Test
    void shouldReturnRoleNotFoundException_WhenToConsultRoleWithIdThatDoesNotExist() {
        Mockito.when(roleRepository.findById(ID)).thenThrow(new RoleNotFoundException(ROLE_NOT_FOUND));

        try {
            roleRepository.findById(ID);

        } catch (Exception ex) {
            assertEquals(RoleNotFoundException.class, ex.getClass());
            assertEquals(ROLE_NOT_FOUND, ex.getMessage());
        }
    }

    private void prepareData() {
        roleOptional = Optional.of(new Role(1L, "ROLE_USER"));
    }

}
