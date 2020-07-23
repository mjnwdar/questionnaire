package com.zzq.system.mapper;

import com.zzq.system.model.LoginLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoginLog loginLog);

    LoginLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(LoginLog loginLog);

    List<LoginLog> selectAll();

    List<Integer> recentlyWeekLoginCount(@Param("username") String username);

    int count();
}