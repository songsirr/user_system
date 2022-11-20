package com.mj.user.advice;

import com.mj.user.domain.response.ApiResponse;
import com.mj.user.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleCustomException(Exception e){
        ApiResponse response = ResponseUtil.exceptionResponseGenerator(e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}
