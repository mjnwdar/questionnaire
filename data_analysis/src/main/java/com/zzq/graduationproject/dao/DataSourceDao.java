package com.zzq.graduationproject.dao;

import com.zzq.graduationproject.model.DataSource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zzq
 */
@Mapper
public interface DataSourceDao {

    /**
     * 查询数据源信息（分页）
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DataSource> getDataSource(@Param("userId") Integer userId,
        @Param("offset") int offset,
        @Param("limit") int limit);


    List<DataSource> selectAllByType(@Param("userId") Integer userId,
        @Param("type") String type);

    int getDataSourceCount(int userId);

    /**
     * 根据id获取数据源
     *
     * @param id
     * @return
     */
    DataSource getDataSourceById(int id);

    /**
     * 添加数据源
     *
     * @param dataSource
     */
    void addDataSource(DataSource dataSource);

    /**
     * 修改数据源信息 不修改：用户、创建时间
     *
     * @param dataSource
     */
    void updateDataSource(DataSource dataSource);

    /**
     * 删除数据源
     *
     * @param id
     */
    void deleteDataSource(int id);

}
