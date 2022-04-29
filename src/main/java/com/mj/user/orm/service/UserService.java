package com.mj.user.orm.service;

import com.mj.user.domain.common.User;
import com.mj.user.domain.request.LoginVO;
import com.mj.user.domain.request.RequestCertificatePhone;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.domain.response.UserInfo;
import com.mj.user.exception.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    /**
     * 인증번호를 반환합니다.
     * @param phone 전화번호
     * @return code 인증번호
     */
    String requestCertificatePhone(String phone) throws InvalidValueException;

    /**
     * 전화번호의 인증을 진행합니다.
     * @param request 전화번호와 인증번호 정보
     * @return phone 인증 완료된 전화번호
     * @throws DuplicatedUserInfoException 중복된 회원정보(전화번호)가 존재할시 예외 발생
     * @throws InvalidCertificateCodeException 인증코드 불일치시 예외 발생
     * @throws NotRequestedCertificatePhoneException 인증요청된 연락처 정보 없을시 예외 발생
     * @throws CertificateTimeOutException 인증 시간제한(5분) 초과시 예외 발생
     */
    String certificatePhone(RequestCertificatePhone request)
            throws InvalidCertificateCodeException,
            NotRequestedCertificatePhoneException, CertificateTimeOutException;

    /**
     * 회원가입을 진행합니다
     * @param signUpVO 회원가입 정보
     * @return User 회원가입시 입력한 정보로 생성된 유저의 정보
     * @throws DuplicatedUserInfoException 이메일, 닉네임, 전화번호의 중복 발생시 예외 발생
     * @throws NotCertificatedPhoneException 인증되지 않은 연락처로 회원가입시 예외 발생
     * @throws CertificateTimeOutException 인증 후 5분 초과시 예외 발생
     * @throws InvalidValueException 형식에 맞지 않는 정보 입력시 예외 발생
     */
    User signUp(SignUpVO signUpVO)
            throws DuplicatedUserInfoException, NotCertificatedPhoneException,
            CertificateTimeOutException, InvalidValueException;

    /**
     * 로그인을 진행합니다.
     * @param loginVO 로그인 정보
     * @return User 로그인한 유저 정보
     * @throws UsernameNotFoundException 회원정보가 없을시 예외 발생
     * @throws InvalidPasswordException 비밀번호 오류시 예외 발생
     */
    User login(LoginVO loginVO) throws UsernameNotFoundException, InvalidPasswordException;

    /**
     * 비밀번호를 재발급 합니다.
     * @param phone 연락처
     * @return password 비밀번호
     * @throws NotCertificatedPhoneException 인증이 완료되지 않은 연락처일 경우 예외 발생
     * @throws CertificateTimeOutException 인증 유효시간이 지난 연락처일 경우 예외 발생
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 경우 예외 발생
     */
    String resetPassword(String phone)
            throws NotCertificatedPhoneException, CertificateTimeOutException, UsernameNotFoundException;

    /**
     * 이메일을 기반으로 로그인한 유저의 정보를 반환합니다.
     * @param email
     * @return UserInfo 로그인한 유저의 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 예외 발생
     */
    UserInfo myInfo(String email) throws UsernameNotFoundException;
}
