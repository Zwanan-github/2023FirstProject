package com.zwan.studyprojectbackend.mapper;

import com.zwan.studyprojectbackend.entity.auth.Account;
import com.zwan.studyprojectbackend.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserAccountMapper {

    /**
     * 根据用户名或者邮箱
     * @param text
     * @return
     */
    @Select("select * from db_account where username = #{text} or email=#{text}")
    AccountUser findAccountByNameOrEmail(String text);

}
