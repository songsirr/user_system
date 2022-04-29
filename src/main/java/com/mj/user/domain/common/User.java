package com.mj.user.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

/**
 * 사용자 테이블
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "사용자 이메일")
    @Column(name = "email", unique = true)
    private String email;

    @ApiModelProperty(notes = "사용자 닉네임")
    @Column(name = "nickname", unique = true)
    private String nickname;

    @ApiModelProperty(notes = "사용자 비밀번호")
    @Column(name = "password")
    private String password;

    @ApiModelProperty(notes = "사용자 이름")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(notes = "사용자 연락처")
    @Column(name = "phone", unique = true)
    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
