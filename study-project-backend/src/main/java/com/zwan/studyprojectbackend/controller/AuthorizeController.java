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

    @PostMapping("/validate-register-email")
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email,
                                          HttpSession session) {
        String response = authorizeService.sendValidateEmail(email, session.getId(), false);
        if (response == null) {
            return RestBean.success("邮件发送成功");
        } else {
            return RestBean.failure(400, response);
        }
    }

    @PostMapping("/validate-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email,
                                          HttpSession session) {
        String response = authorizeService.sendValidateEmail(email, session.getId(), true);
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

    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session){
        String s = authorizeService.validateOnly(email, code, session.getId());
        if (s == null) {
            session.setAttribute("reset-password", email);
            return RestBean.success();
        }else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6, max = 20) @RequestParam("password") String password,
                                          HttpSession session) {
        String email = (String)session.getAttribute("reset-password");
        if (email == null) {
            return RestBean.failure(401, "请先完成邮箱验证");
        }else if (authorizeService.resetPassword(password, email)){
            session.removeAttribute("reset-password");
            return RestBean.success("密码重置成功");
        }else {
            return RestBean.failure(500,"内部错误，请联系管理员");
        }
    }

}
