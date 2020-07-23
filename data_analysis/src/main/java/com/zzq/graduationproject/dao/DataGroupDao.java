package com.zzq.graduationproject.dao;

import com.zzq.graduationproject.model.DataGroup;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zzq
 */
@Mapper
public interface DataGroupDao {


    List<DataGroup> getDataGroup(@Param("userId") Integer userId,
        @Param("offset") int offset,
        @Param("limit") int limit);


    void addDataGroup(DataGroup dataGroup);


    void updateDataGroup(DataGroup dataGroup);

    void deleteDataGroup(Integer id);
}
