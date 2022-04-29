package com.mj.user.exception;

/**
 * 미인증 전화번호 예외
 */
public class NotCertificatedPhoneException extends Exception {

    public NotCertificatedPhoneException() {}

    public NotCertificatedPhoneException(String msg) {
        super(msg);
    }
}
