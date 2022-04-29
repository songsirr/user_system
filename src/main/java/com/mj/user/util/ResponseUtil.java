package com.mj.user.util;

import com.mj.user.domain.response.ApiResponse;

public class ResponseUtil {

    public static ApiResponse exceptionResponseGenerator(String msg){
        ApiResponse response = ApiResponse.builder()
                .status(false)
                .message(msg)
                .build();

        return response;
    }

    public static ApiResponse successResponseGenerator(Object obj){
        ApiResponse response = ApiResponse.builder()
                .status(true)
                .message("SUCCESS")
                .data(obj)
                .build();

        return response;
    }
}
