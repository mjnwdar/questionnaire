package com.zzq.system.mapper;

import com.zzq.system.model.Operator;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OperatorMapper {
    int deleteByPrimaryKey(Integer operatorId);

    int insert(Operator operator);

    Operator selectByPrimaryKey(Integer operatorId);

    int updateByPrimaryKey(Operator operator);

    List<Operator> selectByMenuId(@Param("menuId") Integer menuId);

    List<Operator> selectAll();

    int deleteByMenuId(Integer menuId);
}