package com.mj.user.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST API 용 response
 */
@Data
@NoArgsConstructor
public class ApiResponse {

    @ApiModelProperty(notes = "응답 상태")
    private boolean status;

    @ApiModelProperty(notes = "응답 내용")
    private String message;

    @ApiModelProperty(notes = "응답 데이터")
    private Object data;

    @Builder
    public ApiResponse(boolean status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
}