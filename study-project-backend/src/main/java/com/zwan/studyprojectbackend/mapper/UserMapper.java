package com.zwan.studyprojectbackend.mapper;

import com.zwan.studyprojectbackend.entity.auth.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名或者邮箱登录
     * @param text
     * @return
     */
    @Select("select * from db_account where username = #{text} or email=#{text}")
    Account findAccountByNameOrEmail(String text);

    /**
     * 根据用户名
     * @param username
     * @return
     */
    @Select("select * from db_account where username = #{username}")
    Account findAccountByName(String username);
    /**
     * 根据邮箱
     * @param email
     * @return
     */
    @Select("select * from db_account where email = #{email}")
    Account findAccountByEmail(String email);

    @Insert("insert into db_account (email, username, password) values(#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);

    @Update("update db_account set password=#{password} where email=#{email}")
    boolean resetPasswordbyEmail(String password, String email);
}
