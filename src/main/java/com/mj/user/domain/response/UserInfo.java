package com.mj.user.domain.response;

import com.mj.user.domain.common.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인시 반환할 사용자 정보
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @ApiModelProperty(notes = "사용자 이메일")
    private String email;

    @ApiModelProperty(notes = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(notes = "사용자 이름")
    private String name;

    @ApiModelProperty(notes = "사용자 연락처")
    private String phone;

    public UserInfo(User u){
        this.email = u.getEmail();
        this.name = u.getName();
        this.nickname = u.getNickname();
        this.phone = u.getPhone();
    }
}
