package com.zzq.graduationproject.dao;

import com.zzq.graduationproject.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zzq
 */
@Mapper
public interface UserDao {

    /**
     * 添加用户
     *
     * @param user
     */
    void addUser(User user);

    /**
     * 根据登录名查询用户信息
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 修改用户密码
     *
     * @param user
     */
    void updatePassword(User user);

    /**
     * 根据id删除用户
     *
     * @param userId 待删除用户id
     */
    void deleteUserById(int userId);

    /**
     * 根据用户名删除用户
     *
     * @param username 待删除用户用户名
     */
    void deleteUserByUsername(String username);
}
