package com.mj.user.exception;


/**
 * 인증 요청 없는 연락처 예외
 */
public class NotRequestedCertificatePhoneException extends Exception {

    public NotRequestedCertificatePhoneException(){}

    public NotRequestedCertificatePhoneException(String msg) {
        super(msg);
    }
}
