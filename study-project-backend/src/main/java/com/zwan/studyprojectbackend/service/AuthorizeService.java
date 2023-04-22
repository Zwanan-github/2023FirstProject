package com.zwan.studyprojectbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    /**
     * 发送邮件
     * @param email
     * @param sessionDd
     * @param hasAccount 是否需要存在账户
     * @return
     */
    String sendValidateEmail(String email, String sessionDd, boolean hasAccount);


    /**
     * 注册前的邮箱认证
     * @param username
     * @param password
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    String validateAndRegister(String username, String password, String email, String code, String sessionId);

    /**
     * 重置密码前的邮箱认证
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    String validateOnly(String email, String code, String sessionId);

    boolean resetPassword(String password, String email);

}
