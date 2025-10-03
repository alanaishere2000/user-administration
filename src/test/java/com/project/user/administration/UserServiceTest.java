package com.project.user.administration.services;

import com.project.user.administration.model.User;
import com.project.user.administration.model.UserLogin;
import com.project.user.administration.repository.UserLoginRepository;
import com.project.user.administration.repository.UserRepository;
import com.project.user.administration.vo.UserRequestVo;
import com.project.user.administration.vo.UserTokenResponseVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserLoginRepository userLoginRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userLoginRepository = mock(UserLoginRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserService();
        userService.userRepository = userRepository;
        userService.userLoginRepository = userLoginRepository;
        userService.bCryptPasswordEncoder = passwordEncoder;
    }

    @Test
    void testValidateUserCredentialsAndGenerateToken_success() {
        UserRequestVo requestVo = UserRequestVo.builder()
                .username("testuser")
                .password("1234")
                .build();

        User mockUser = User.builder()
                .userName("testuser")
                .password("encodedPass")
                .build();

        when(userRepository.findUserByStatusAndName("testuser")).thenReturn(mockUser);
        when(passwordEncoder.matches("1234", "encodedPass")).thenReturn(true);

        UserTokenResponseVo response = userService.validateUserCredentialsAndGenerateToken(requestVo);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertNotNull(response.getToken());
    }

    @Test
    void testValidateUserCredentialsAndGenerateToken_fail() {
        UserRequestVo requestVo = UserRequestVo.builder()
                .username("wronguser")
                .password("wrongpass")
                .build();

        when(userRepository.findUserByStatusAndName("wronguser")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.validateUserCredentialsAndGenerateToken(requestVo);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
