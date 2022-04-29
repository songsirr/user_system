package com.mj.user;

import com.mj.user.domain.common.User;
import com.mj.user.domain.request.LoginVO;
import com.mj.user.exception.InvalidPasswordException;
import com.mj.user.orm.repository.CertificatePhoneRepository;
import com.mj.user.orm.repository.UserRepository;
import com.mj.user.orm.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CertificatePhoneRepository certificatePhoneRepository;

    UserServiceImpl userService;

    private User testUser;

    private LoginVO loginVO;

    @BeforeEach
    void setup(){
        loginVO = new LoginVO().builder()
                .value("01012341234")
                .password("thd@1234")
                .build();

        testUser = new User().builder()
                .id(Long.getLong("1"))
                .name("송명준")
                .email("songsirr@mj.song")
                .nickname("mjsong")
                .phone("01012341234")
                .password("thd@1234")
                .build();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserServiceImpl(userRepository, certificatePhoneRepository, passwordEncoder);
    }

    @Test
    void caseNotFoundUser(){
        when(userRepository.findByPhone("01012341234")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(loginVO);
        });
    }

    @Test
    void caseInvalidPassword(){
        when(userRepository.findByPhone("01012341234")).thenReturn(Optional.ofNullable(testUser));

        loginVO.setPassword("1234@thd");

        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            userService.login(loginVO);
        });
    }

    @Test
    void caseSuccessLogin() throws Exception{
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        testUser.setPassword(passwordEncoder.encode(testUser.getPassword()));
        when(userRepository.findByPhone("01012341234")).thenReturn(Optional.ofNullable(testUser));
        Assertions.assertEquals(userService.login(loginVO).getPhone(), loginVO.getValue());
    }
}
