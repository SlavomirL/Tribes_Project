package com.gfa.tribesvibinandtribinotocyon.service;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import com.gfa.tribesvibinandtribinotocyon.repositories.RoleRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import com.gfa.tribesvibinandtribinotocyon.services.UserServiceImpl;
import com.gfa.tribesvibinandtribinotocyon.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doesUserExist_ExistingUser_ReturnsTrue() {
        String username = "existingUser";
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));

        assertTrue(userService.doesUserExist(username));
    }

    @Test
    void doesUserExist_NonExistingUser_ReturnsFalse() {
        String username = "nonExistingUser";
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertFalse(userService.doesUserExist(username));
    }



    @Test
    void createUser_UserAlreadyExists_ReturnsEmptyOptional() throws TribesInternalServerErrorException, IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newUser");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@example.com");
        when(userEntityRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new UserEntity()));

        Optional<UserEntity> createdUser = userService.createUser(registerRequest);

        assertTrue(createdUser.isEmpty());
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }
}
