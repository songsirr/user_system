package com.mj.user.exception;

/**
 * 비밀번호 불일치 예외
 */
public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(){}

    public InvalidPasswordException(String msg){
        super(msg);
    }
}
