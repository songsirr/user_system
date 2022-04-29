package com.mj.user.exception;

/**
 * 중복된 유저 정보에 대한 예외
 */
public class DuplicatedUserInfoException extends Exception {

    public DuplicatedUserInfoException(){}

    public DuplicatedUserInfoException(String msg){
        super(msg);
    }
}
