package com.zwan.studyprojectbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface AuthorizeService extends UserDetailsService {
    /**
     * 发送邮件
     * @param email
     * @return
     */
    boolean sendValidateEmail(String email, String sessionDd);

}
