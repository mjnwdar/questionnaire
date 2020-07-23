package com.zzq.system.mapper;

import com.zzq.system.model.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role role);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKey(Role role);

    List<Role> selectAll();

    List<Role> selectAllByQuery(Role roleQuery);

    int count();
}