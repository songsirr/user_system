package com.mj.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 로그인 VO
 */
@Data
public class LoginVO {

    @ApiModelProperty(notes = "이메일 or 닉네임 or 연락처")
    private String value;

    @ApiModelProperty(notes = "사용자 비밀번호")
    private String password;
}
