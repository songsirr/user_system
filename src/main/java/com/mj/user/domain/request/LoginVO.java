package com.mj.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginVO {

    @ApiModelProperty(notes = "이메일 or 닉네임 or 연락처")
    private String value;

    @ApiModelProperty(notes = "사용자 비밀번호")
    private String password;
}
