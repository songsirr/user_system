package com.mj.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 로그인 VO
 */
@Data
public class LoginVO {

    @ApiModelProperty(notes = "사용자 이메일")
    private String email;

    @ApiModelProperty(notes = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(notes = "사용자 연락처")
    private String phone;

    @ApiModelProperty(notes = "사용자 비밀번호")
    private String password;
}
