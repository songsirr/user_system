package com.mj.user.controller;

import com.mj.user.domain.request.LoginVO;
import com.mj.user.domain.request.SignUpVO;
import com.mj.user.domain.response.UserInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public void certificatePhone(@RequestBody String phone){

    }

    public void signUp(@RequestBody SignUpVO signUpVO){

    }

    public void login(@RequestBody LoginVO loginVO){

    }

    public void resetPasswordByPhone(@RequestBody String phone){

    }
}
