package com.zwan.studyprojectbackend.service.impl;

import com.zwan.studyprojectbackend.entity.Account;
import com.zwan.studyprojectbackend.mapper.UserMapper;
import com.zwan.studyprojectbackend.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Autowired
    private UserMapper userMapper;

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
        return false;
    }
}
