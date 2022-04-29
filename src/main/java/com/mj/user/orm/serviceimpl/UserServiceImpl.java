package com.mj.user.orm.serviceimpl;

import com.mj.user.domain.common.CertificatePhone;
import com.mj.user.domain.common.User;
import com.mj.user.domain.request.LoginVO;
import com.mj.user.domain.request.RequestCertificatePhone;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.domain.response.UserInfo;
import com.mj.user.exception.*;
import com.mj.user.orm.repository.CertificatePhoneRepository;
import com.mj.user.orm.repository.UserRepository;
import com.mj.user.orm.service.UserService;
import com.mj.user.util.NumberUtil;
import com.mj.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CertificatePhoneRepository certificatePhoneRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String requestCertificatePhone(String phone) throws InvalidValueException {
        Date now = new Date();

        // validation phone
        String regex = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        if(!matcher.matches()){
            throw new InvalidValueException("INVALID PHONE");
        }

        phone = phone.replace("-", "");
        phone = phone.replace(".", "");

        certificatePhoneRepository.deleteAllByPhone(phone);

        String code = NumberUtil.randomNumberGenerator(6);

        CertificatePhone certificatePhone = CertificatePhone.builder()
                .phone(phone)
                .code(code)
                .requestedAt(now)
                .build();

        certificatePhoneRepository.save(certificatePhone);

        return code;
    }

    @Override
    @Transactional
    public String certificatePhone(RequestCertificatePhone request)
            throws InvalidCertificateCodeException,
            NotRequestedCertificatePhoneException, CertificateTimeOutException {

        String phone = request.getPhone();
        String code = request.getCode();

        Optional<CertificatePhone> op = certificatePhoneRepository.findByPhoneAndCode(phone, code);

        if (!op.isPresent()){
            if (!certificatePhoneRepository.findByPhone(phone).isPresent()){ // check user requested certificate or not
                throw new NotRequestedCertificatePhoneException();
            } else {
                throw new InvalidCertificateCodeException();
            }
        } else {
            Date now = new Date();
            CertificatePhone certificatePhone = op.get();
            if (now.getTime() - certificatePhone.getRequestedAt().getTime() > 300000){ // check certificate timeout (over 5 min)
                throw new CertificateTimeOutException();
            }
            certificatePhone.setCertificatedAt(now);
            certificatePhoneRepository.save(certificatePhone);
            return request.getPhone();
        }
    }

    @Override
    @Transactional
    public User signUp(SignUpVO signUpVO)
            throws DuplicatedUserInfoException, NotCertificatedPhoneException,
            CertificateTimeOutException, InvalidValueException {

        Optional<CertificatePhone> cp =  certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(signUpVO.getPhone());

        // check certificated or not phone
        if (!cp.isPresent()){
            throw new NotCertificatedPhoneException();
        }

        // check certificate timeout (over 5 min)
        Date now = new Date();
        if (now.getTime() - cp.get().getCertificatedAt().getTime() > 300000){
            throw new CertificateTimeOutException();
        }

        // check duplicated value of user
        if (userRepository.findByEmail(signUpVO.getEmail()).isPresent()){
            throw new DuplicatedUserInfoException("DUPLICATED EMAIL");
        } else if (userRepository.findByNickname(signUpVO.getNickname()).isPresent()) {
            throw new DuplicatedUserInfoException("DUPLICATED NICKNAME");
        } else if (userRepository.findByPhone(signUpVO.getPhone()).isPresent()) {
            throw new DuplicatedUserInfoException("DUPLICATED PHONE");
        }

        // check null or empty value for name, nickname
        if (signUpVO.getNickname() == null || signUpVO.getNickname().isEmpty()){
            throw new InvalidValueException("INVALID NICKNAME");
        } else if (signUpVO.getName() == null || signUpVO.getName().isEmpty()){
            throw new InvalidValueException("INVALID NAME");
        }

        // validation email
        Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(signUpVO.getEmail());
        if (!matcher.matches()){
            throw new InvalidValueException("INVALID EMAIL");
        }

        // validation password
        pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$");
        matcher = pattern.matcher(signUpVO.getPassword());
        if (!matcher.matches()){
            throw new InvalidValueException("INVALID PASSWORD");
        }

        User newUser = User.builder()
                .email(signUpVO.getEmail())
                .password(passwordEncoder.encode(signUpVO.getPassword()))
                .nickname(signUpVO.getNickname())
                .name(signUpVO.getName())
                .phone(signUpVO.getPhone())
                .build();

        certificatePhoneRepository.deleteAllByPhone(signUpVO.getPhone());

        return userRepository.save(newUser);
    }

    @Override
    public User login(LoginVO loginVO) throws UsernameNotFoundException, InvalidPasswordException{

        User u = null;
        if (userRepository.findByEmail(loginVO.getValue()).isPresent()){
            u = userRepository.findByEmail(loginVO.getValue()).get(); // login with email
        } else if (userRepository.findByNickname(loginVO.getValue()).isPresent()) {
            u = userRepository.findByNickname(loginVO.getValue()).get(); // login with nickname
        } else if (userRepository.findByPhone(loginVO.getValue()).isPresent()){
            u = userRepository.findByPhone(loginVO.getValue()).get(); // login with phone
        } else {
            throw new UsernameNotFoundException("");
        }

        // check user password
        if (passwordEncoder.matches(loginVO.getPassword(), u.getPassword())){
            return u;
        } else {
            throw new InvalidPasswordException();
        }
    }

    @Override
    @Transactional
    public String resetPassword(String phone)
            throws NotCertificatedPhoneException, CertificateTimeOutException, UsernameNotFoundException{

        // check exist user with phone
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (!optionalUser.isPresent()){
            throw new UsernameNotFoundException("");
        }

        // check certificated or not phone
        Optional<CertificatePhone> cp =  certificatePhoneRepository.findByPhoneAndCertificatedAtIsNotNull(phone);
        if (!cp.isPresent()){
            throw new NotCertificatedPhoneException();
        }

        // check certificate timeout (over 5 min)
        Date now = new Date();
        if (now.getTime() - cp.get().getCertificatedAt().getTime() > 300000){
            throw new CertificateTimeOutException();
        }

        String password = PasswordUtil.gen();
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        certificatePhoneRepository.deleteAllByPhone(phone);

        return password;
    }

    @Override
    public UserInfo myInfo(String email) throws UsernameNotFoundException{
        User u = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));
        UserInfo userInfo = new UserInfo(u);
        return userInfo;
    }
}
