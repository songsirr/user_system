package com.mj.user.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST API 용 response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    @ApiModelProperty(notes = "응답 상태")
    private boolean status;

    @ApiModelProperty(notes = "응답 내용")
    private String message;

    @ApiModelProperty(notes = "응답 데이터")
    private Object data;

}