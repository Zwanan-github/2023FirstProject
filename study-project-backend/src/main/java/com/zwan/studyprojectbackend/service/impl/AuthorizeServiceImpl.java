package com.zwan.studyprojectbackend.service.impl;

import com.zwan.studyprojectbackend.entity.Account;
import com.zwan.studyprojectbackend.mapper.UserMapper;
import com.zwan.studyprojectbackend.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    MailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }


    @Override
    public boolean sendValidateEmail(String email) {
        /**
         * 1. 先生成对应的验证码
         * 2. 把邮箱和对应的验证码放到Redis中， 过期时间（重复发的间断大于60，重复流程）
         * 3. 发送邮件
         * 4. 发送失败， 删除Redis中的该键值对
         * 5. 用户注册时，从Redis中取出来对比
         */
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("验证码是：" + code);
        try{
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
