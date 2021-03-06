package com.mj.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpVO {

    @ApiModelProperty(notes = "사용자 이메일")
    private String email;

    @ApiModelProperty(notes = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(notes = "사용자 비밀번호")
    private String password;

    @ApiModelProperty(notes = "사용자 이름")
    private String name;

    @ApiModelProperty(notes = "사용자 연락처")
    private String phone;
}
