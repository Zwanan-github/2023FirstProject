package com.zwan.studyprojectbackend.service.impl;

import com.zwan.studyprojectbackend.entity.auth.Account;
import com.zwan.studyprojectbackend.mapper.UserMapper;
import com.zwan.studyprojectbackend.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Resource
    MailSender mailSender;

    @Autowired
    StringRedisTemplate template;

    // 循环植入
//    @Autowired
//    BCryptPasswordEncoder encoder;

    @Value("${spring.mail.username}")
    String from;

    /**
     * 登陆验证
     * @param text
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String text) throws UsernameNotFoundException {
        if (text == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByNameOrEmail(text);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    /**
     * 发送验证码
     * @param email
     * @param sessionId
     * @param hasAccount 是否需要存在账户
     * @return
     */
    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {
        /**
         * 1. 先生成对应的验证码
         * 2. 把邮箱和对应的验证码放到Redis中， 过期时间（重复发的间断大于60，重复流程）
         * 3. 发送邮件
         * 4. 发送失败， 删除Redis中的该键值对
         * 5. 用户注册时，从Redis中取出来对比
         */
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        // 判断时间
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            // 剩余时间大于60，拦截
            if (expire > 60) {
                return "请勿连续发送邮件";
            }
        }
        Account accountByEmail = userMapper.findAccountByEmail(email);
        // 找回密码时（hasAccount == true, accountByEmail != null 才能发送）
        if (hasAccount && accountByEmail == null) {
            return "没有此账户";
        }

        // 注册时（hasAccount == false, accountByEmail == null 时候才能发送）
        if (!hasAccount && accountByEmail != null) {
            return "此账户已存在";
        }

        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("验证码是：" + code);
        try{
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code), 2, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮箱是否有效";
        }
    }

    /**
     * 注册功能
     * @param username
     * @param password
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    @Override
    public String validateAndRegister(String username,
                                       String password,
                                       String email,
                                       String code,
                                       String sessionId) {
        // 用户名唯一
        if (userMapper.findAccountByName(username) != null) {
            return "用户名已存在";
        }
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if (s == null) {
                return "验证码失效";
            }
            if (s.equals(code)){
                password = encoder.encode(password);
                if (userMapper.createAccount(username, password, email) > 0) {
                    // 删除redis中存的验证码
                    template.delete(key);
                    return null;
                }else {
                    return "内部错误，请联系管理员";
                }
            }else {
                return "验证码错误，请检查一下";
            }
        } else {
            return "请先请求验证码";
        }
    }

    /**
     * 重置密码前的邮箱认证
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if (s == null) {
                return "验证码失效";
            }
            if (s.equals(code)){
                // 删除redis中存的验证码
                template.delete(key);
                return null;
            }else {
                return "验证码错误，请检查一下";
            }
        } else {
            return "请先请求验证码";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password);
        return userMapper.resetPasswordbyEmail(password, email);
    }
}
