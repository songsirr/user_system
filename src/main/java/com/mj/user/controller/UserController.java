package com.mj.user.controller;

import com.mj.user.constant.UrlConstant;
import com.mj.user.domain.common.User;
import com.mj.user.domain.request.LoginVO;
import com.mj.user.domain.request.RequestCertificatePhone;
import com.mj.user.domain.request.RequestPhone;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.domain.response.ApiResponse;
import com.mj.user.orm.service.UserService;
import com.mj.user.util.JwtTokenProvider;
import com.mj.user.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"User"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlConstant.URI_USER)
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "연락처 인증 요청 API", notes = "연락처 인증을 요청합니다.")
    @PostMapping(value = UrlConstant.URI_CERTIFICATE + UrlConstant.URI_REQUEST)
    public ResponseEntity<ApiResponse> requestCertificatePhone(@RequestBody RequestPhone requestPhone) throws Exception {
        ApiResponse response = null;
        String code = userService.requestCertificatePhone(requestPhone.getPhone());
        response = ResponseUtil.successResponseGenerator(code);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "연락처 인증 API", notes = "연락처 인증을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_CERTIFICATE)
    public ResponseEntity<ApiResponse> certificatePhone(@RequestBody RequestCertificatePhone request) throws Exception {
        ApiResponse response = null;
        String phone = userService.certificatePhone(request);
        response = ResponseUtil.successResponseGenerator(phone);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "회원가입 API", notes = "회원가입을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_SIGNUP)
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpVO signUpVO) throws Exception {
        ApiResponse response = null;
        userService.signUp(signUpVO);
        response = ResponseUtil.successResponseGenerator(null);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "로그인 API", notes = "로그인을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_LOGIN)
    public ResponseEntity<ApiResponse> login(@RequestBody LoginVO loginVO) throws Exception {
        ApiResponse response = null;
        User u = userService.login(loginVO);
        String token = jwtTokenProvider.createToken(u.getEmail());
        response = ResponseUtil.successResponseGenerator(token);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "비밀번호 재발급 API", notes = "연락처로 회원을 찾아 비밀번호를 재발급합니다.")
    @PostMapping(value = UrlConstant.URI_RESET_PASSWORD)
    public ResponseEntity<ApiResponse> resetPasswordByPhone(@RequestBody RequestPhone requestPhone) throws Exception {
        ApiResponse response = null;
        String password = userService.resetPassword(requestPhone.getPhone());
        response = ResponseUtil.successResponseGenerator(password);
        return ResponseEntity.ok(response);
    }
}
