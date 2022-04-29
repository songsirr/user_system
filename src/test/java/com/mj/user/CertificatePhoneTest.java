package com.mj.user;

import com.mj.user.domain.common.CertificatePhone;
import com.mj.user.domain.request.RequestCertificatePhone;
import com.mj.user.exception.CertificateTimeOutException;
import com.mj.user.exception.InvalidCertificateCodeException;
import com.mj.user.exception.InvalidValueException;
import com.mj.user.exception.NotRequestedCertificatePhoneException;
import com.mj.user.orm.repository.CertificatePhoneRepository;
import com.mj.user.orm.repository.UserRepository;
import com.mj.user.orm.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificatePhoneTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CertificatePhoneRepository certificatePhoneRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    UserServiceImpl userService;

    private CertificatePhone certificatePhone;

    private RequestCertificatePhone requestCertificatePhone;

    @BeforeEach
    void setup(){
        Date now = new Date();

        certificatePhone = new CertificatePhone().builder()
                .phone("01012341234")
                .code("12345678")
                .requestedAt(now)
                .certificatedAt(now)
                .build();

        requestCertificatePhone = new RequestCertificatePhone().builder()
                .phone("01012341234")
                .code("12345678")
                .build();

        userService = new UserServiceImpl(userRepository, certificatePhoneRepository, passwordEncoder);
    }

    @Test
    void caseInvalidPhone(){
        Assertions.assertThrows(InvalidValueException.class, () -> {
            userService.requestCertificatePhone("123");
        });
    }

    @Test
    void caseNotRequestedPhone(){
        Assertions.assertThrows(NotRequestedCertificatePhoneException.class, () -> {
            userService.certificatePhone(requestCertificatePhone);
        });
    }

    @Test
    void caseInvalidCode(){
        when(certificatePhoneRepository.findByPhone(any())).thenReturn(Optional.ofNullable(certificatePhone));
        when(certificatePhoneRepository.findByPhoneAndCode(any(), any())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidCertificateCodeException.class, () -> {
            userService.certificatePhone(requestCertificatePhone);
        });
    }

    @Test
    void caseCertificateTimeout(){
        Date before = new Date(System.currentTimeMillis() - 3600 * 1000);
        certificatePhone.setRequestedAt(before);
        certificatePhone.setCertificatedAt(null);
        when(certificatePhoneRepository.findByPhoneAndCode(any(), any())).thenReturn(Optional.ofNullable(certificatePhone));

        Assertions.assertThrows(CertificateTimeOutException.class, () -> {
           userService.certificatePhone(requestCertificatePhone);
        });
    }

    @Test
    void caseSuccessCertificatePhone() throws Exception{
        when(certificatePhoneRepository.findByPhoneAndCode(any(), any())).thenReturn(Optional.ofNullable(certificatePhone));

        Assertions.assertEquals(userService.certificatePhone(requestCertificatePhone), "01012341234");
    }
}
