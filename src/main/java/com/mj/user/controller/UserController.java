package com.mj.user.controller;

import com.mj.user.constant.UrlConstant;
import com.mj.user.domain.common.User;
import com.mj.user.domain.request.LoginVO;
import com.mj.user.domain.request.RequestCertificatePhone;
import com.mj.user.domain.request.RequestPhone;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.domain.response.ApiResponse;
import com.mj.user.exception.*;
import com.mj.user.orm.service.UserService;
import com.mj.user.util.JwtTokenProvider;
import com.mj.user.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<ApiResponse> requestCertificatePhone(@RequestBody RequestPhone requestPhone){
        ApiResponse response = null;
        try {
            String code = userService.requestCertificatePhone(requestPhone.getPhone());
            response = ResponseUtil.successResponseGenerator(code);
        } catch (InvalidValueException ive){
            ive.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator(ive.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("INTERNAL SERVER ERROR WHILE REQUEST CERTIFICATE PHONE");
        } finally {
            return ResponseEntity.ok(response);
        }
    }

    @ApiOperation(value = "연락처 인증 API", notes = "연락처 인증을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_CERTIFICATE)
    public ResponseEntity<ApiResponse> certificatePhone(@RequestBody RequestCertificatePhone request){
        ApiResponse response = null;
        try {
            String phone = userService.certificatePhone(request);
            response = ResponseUtil.successResponseGenerator(phone);
        } catch (InvalidCertificateCodeException icce){
            icce.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("INVALID CERTIFICATE CODE");
        } catch (NotRequestedCertificatePhoneException nrcpe){
            nrcpe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("NOT REQUESTED CERTIFICATE PHONE");
        } catch (CertificateTimeOutException ctoe){
            ctoe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("CERTIFICATE TIMEOUT");
        }catch (Exception e){
            response = ResponseUtil.exceptionResponseGenerator("INTERNAL SERVER ERROR WHILE CERTIFICATE PHONE");
        } finally {
            return ResponseEntity.ok(response);
        }
    }

    @ApiOperation(value = "회원가입 API", notes = "회원가입을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_SIGNUP)
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpVO signUpVO){
        ApiResponse response = null;
        try {
            userService.signUp(signUpVO);
            response = ResponseUtil.successResponseGenerator(null);
        } catch (NotCertificatedPhoneException ncpe){
            ncpe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("NOT CERTIFICATED PHONE");
        } catch (DuplicatedUserInfoException | InvalidValueException nmve){
            nmve.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator(nmve.getMessage());
        } catch (CertificateTimeOutException ctoe){
            ctoe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("CERTIFICATE TIMEOUT");
        } catch (Exception e){
            e.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("INTERNAL SERVER ERROR WHILE SIGNUP");
        } finally {
            return ResponseEntity.ok(response);
        }
    }

    @ApiOperation(value = "로그인 API", notes = "로그인을 진행합니다.")
    @PostMapping(value = UrlConstant.URI_LOGIN)
    public ResponseEntity<ApiResponse> login(@RequestBody LoginVO loginVO){
        ApiResponse response = null;
        try {
            User u = userService.login(loginVO);
            String token = jwtTokenProvider.createToken(u.getEmail());
            response = ResponseUtil.successResponseGenerator(token);
        } catch (UsernameNotFoundException unfe){
            unfe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("CAN NOT FIND USER");
        } catch (InvalidPasswordException ipe){
            ipe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("INVALID PASSWORD");
        } catch (Exception e){
            e.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("INTERNAL SERVER ERROR WHILE LOGIN");
        } finally {
            return ResponseEntity.ok(response);
        }
    }

    @ApiOperation(value = "비밀번호 재발급 API", notes = "연락처로 회원을 찾아 비밀번호를 재발급합니다.")
    @PostMapping(value = UrlConstant.URI_RESET_PASSWORD)
    public ResponseEntity<ApiResponse> resetPasswordByPhone(@RequestBody RequestPhone requestPhone){
        ApiResponse response = null;
        try {
            String password = userService.resetPassword(requestPhone.getPhone());
            response = ResponseUtil.successResponseGenerator(password);
        } catch (UsernameNotFoundException unfe){
            unfe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("CAN NOT FIND USER");
        } catch (NotCertificatedPhoneException ncpe){
            ncpe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("NOT CERTIFICATED PHONE");
        } catch (CertificateTimeOutException ctoe){
            ctoe.printStackTrace();
            response = ResponseUtil.exceptionResponseGenerator("CERTIFICATE TIMEOUT");
        }catch (Exception e){
            response = ResponseUtil.exceptionResponseGenerator("INTERNAL SERVER ERROR WHILE RESET PASSWORD");
        } finally {
            return ResponseEntity.ok(response);
        }
    }
}
