package com.mj.user;

import com.mj.user.domain.common.CertificatePhone;
import com.mj.user.domain.common.User;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.exception.CertificateTimeOutException;
import com.mj.user.exception.DuplicatedUserInfoException;
import com.mj.user.exception.InvalidValueException;
import com.mj.user.exception.NotCertificatedPhoneException;
import com.mj.user.orm.repository.CertificatePhoneRepository;
import com.mj.user.orm.repository.UserRepository;
import com.mj.user.orm.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSignUpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CertificatePhoneRepository certificatePhoneRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    UserServiceImpl userService;

    private SignUpVO signUpVO;

    private CertificatePhone certificatePhone;

    private User testUser;

    @BeforeEach
    void setup(){
        signUpVO = new SignUpVO().builder()
                .name("송명준")
                .email("songsirr@mj.song")
                .nickname("mjsong")
                .phone("01012341234")
                .password("thd@1234")
                .build();

        Date now = new Date();

        certificatePhone = new CertificatePhone().builder()
                .phone("01012341234")
                .code("12345678")
                .requestedAt(now)
                .certificatedAt(now)
                .build();

        testUser = new User().builder()
                .id(Long.getLong("1"))
                .name("송명준")
                .email("songsirr@mj.song")
                .nickname("mjsong")
                .phone("01012341234")
                .password(passwordEncoder.encode("thd@1234"))
                .build();

        userService = new UserServiceImpl(userRepository, certificatePhoneRepository, passwordEncoder);
    }

    @Test
    void caseInvalid(){

        when(certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(any())).thenReturn(Optional.ofNullable(certificatePhone));

        // test if name has white space
        signUpVO.setName("송 명준");
        Assertions.assertThrows(InvalidValueException.class, () -> {
            userService.signUp(signUpVO);
        });

        // test if nickname has white space
        signUpVO.setNickname("mj song");
        Assertions.assertThrows(InvalidValueException.class, () -> {
            userService.signUp(signUpVO);
        });

        // test if invalid email
        signUpVO.setEmail("asdasdasd");
        Assertions.assertThrows(InvalidValueException.class, () -> {
            userService.signUp(signUpVO);
        });

        // test if invalid password
        signUpVO.setPassword("12312312");
        Assertions.assertThrows(InvalidValueException.class, () -> {
            userService.signUp(signUpVO);
        });
    }

    @Test
    void caseNotCertificatedPhone(){
        when(certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(any())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NotCertificatedPhoneException.class, () -> {
            userService.signUp(signUpVO);
        });
    }

    @Test
    void caseCertificateTimeout(){
        certificatePhone.setCertificatedAt(new Date(System.currentTimeMillis() - 3600 * 1000));
        when(certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(any())).thenReturn(Optional.ofNullable(certificatePhone));

        Assertions.assertThrows(CertificateTimeOutException.class, () -> {
            userService.signUp(signUpVO);
        });
    }

    @Test
    void caseDuplicatedUserInfo(){
        when(certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(any())).thenReturn(Optional.ofNullable(certificatePhone));
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(testUser));

        Assertions.assertThrows(DuplicatedUserInfoException.class, () -> {
            userService.signUp(signUpVO);
        });
    }

    @Test
    void caseSuccessSignup() throws Exception{
        when(certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(any())).thenReturn(Optional.ofNullable(certificatePhone));
        when(userRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        Assertions.assertEquals(userService.signUp(signUpVO).getEmail(), testUser.getEmail());
    }
}
