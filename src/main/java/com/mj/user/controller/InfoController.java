package com.mj.user.controller;

import com.mj.user.constant.UrlConstant;
import com.mj.user.domain.response.ApiResponse;
import com.mj.user.domain.response.UserInfo;
import com.mj.user.orm.service.UserService;
import com.mj.user.util.JwtTokenProvider;
import com.mj.user.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"Info"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlConstant.URI_INFO)
public class InfoController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "내 정보 보기 API", notes = "내 정보를 봅니다.")
    @GetMapping(value = UrlConstant.URI_MY)
    public ResponseEntity<ApiResponse> requestCertificatePhone(HttpServletRequest request) throws Exception {
        ApiResponse response = null;
        String email = jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request));
        UserInfo userInfo = userService.myInfo(email);
        response = ResponseUtil.successResponseGenerator(userInfo);
        return ResponseEntity.ok(response);
    }
}
