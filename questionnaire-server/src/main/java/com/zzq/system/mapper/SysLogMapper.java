package com.zzq.system.mapper;

import com.zzq.system.model.SysLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLog sysLog);

    SysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysLog sysLog);

    List<SysLog> selectAll();

    int count();
}