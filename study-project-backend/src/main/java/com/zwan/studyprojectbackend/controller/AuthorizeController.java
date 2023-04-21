package com.zwan.studyprojectbackend.controller;

import com.zwan.studyprojectbackend.entity.RestBean;
import com.zwan.studyprojectbackend.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_VALID = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";

    @Autowired
    AuthorizeService authorizeService;

    @PostMapping("/validate-email")
    public RestBean validateEmail(@Pattern(regexp = EMAIL_VALID) @RequestParam("email") String email) {
        if (authorizeService.sendValidateEmail(email)) {
            return RestBean.success("邮件发送成功");
        }else {
            return RestBean.failure(400 , "邮件发送失败，请联系管理员");
        }
    }

}
