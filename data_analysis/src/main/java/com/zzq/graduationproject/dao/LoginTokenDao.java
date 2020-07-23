package com.zzq.graduationproject.dao;

import com.zzq.graduationproject.model.LoginToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zzq
 */
@Mapper
public interface LoginTokenDao {

    String TABLE_NAME = " login_token ";
    String INSERT_FIELDS = " user_id, token, expired, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") ",
        "values(#{userId}, #{token}, #{expired}, 0);"})
    int addToken(LoginToken loginToken);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
        "where token=#{token}"})
    LoginToken getLoginTokenByToken(String token);


    @Select({"update ", TABLE_NAME, "set status=#{status} where token=#{token};"})
    void updateStatus(@Param("token") String token,
        @Param("status") int status);
}
