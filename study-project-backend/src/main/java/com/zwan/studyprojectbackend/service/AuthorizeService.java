package com.zwan.studyprojectbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;


public interface AuthorizeService extends UserDetailsService {
    /**
     * 发送邮件
     * @param
     * @return
     */
    String sendValidateEmail(String email, String sessionDd);


    /**
     * 注册
     * @param username
     * @param password
     * @param email
     * @param code
     * @param session
     * @return
     */
    String validateAndRegister(String username, String password, String email, String code, String session);

}
