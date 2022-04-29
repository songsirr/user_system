package com.mj.user.exception;

/**
 * 인증번호 불일치 예외
 */
public class InvalidCertificateCodeException extends Exception {

    public InvalidCertificateCodeException() {}

    public InvalidCertificateCodeException(String msg) {
        super(msg);
    }
}
