package com.zwan.studyprojectbackend.controller;

import com.zwan.studyprojectbackend.entity.RestBean;
import com.zwan.studyprojectbackend.service.AuthorizeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_VALID = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    private final String USERNAME_VALID = "^.{1,20}$";

    @Autowired
    AuthorizeService authorizeService;

    @PostMapping("/validate-email")
    public RestBean<String> validateEmail(@Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email,
                                          HttpSession session) {
        String response = authorizeService.sendValidateEmail(email, session.getId());
        if (response == null) {
            return RestBean.success("邮件发送成功");
        } else {
            return RestBean.failure(400, response);
        }
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_VALID) @RequestParam("username") String username,
                                         @Length(min = 6, max = 20) @RequestParam("password") String password,
                                         @Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email,
                                         @Length(min = 6, max = 6) @RequestParam("code") String code,
                                         HttpSession session) {
        String response = authorizeService.validateAndRegister(username, password, email, code, session.getId());
        if (null == response) {
            return RestBean.success("注册成功");
        } else {
            return RestBean.failure(400,response);
        }
    }

}
