package com.zzq.system.controller;

import com.github.pagehelper.PageInfo;
import com.zzq.common.annotation.OperationLog;
import com.zzq.common.util.PageResultBean;
import com.zzq.common.util.ResultEntity;
import com.zzq.common.validate.groups.Create;
import com.zzq.system.model.User;
import com.zzq.system.service.RoleService;
import com.zzq.system.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @OperationLog("获取用户列表")
    @GetMapping("/list")
    public PageResultBean<User> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        User userQuery) {
        List<User> users = userService.selectAllWithDept(page, limit, userQuery);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }


    @GetMapping("/{userId}")
    public String update(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("roleIds", userService.selectRoleIdsById(userId));
        model.addAttribute("user", userService.selectOne(userId));
        model.addAttribute("roles", roleService.selectAll());
        return "user/user-add";
    }

    @OperationLog("编辑角色")
    @PutMapping
    public ResultEntity update(@Valid User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        userService.update(user, roleIds);
        return ResultEntity.success();
    }

    @OperationLog("新增用户")
    @PostMapping
    public ResultEntity add(@Validated(Create.class) User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        return ResultEntity.success(userService.add(user, roleIds));
    }

    @OperationLog("禁用账号")
    @PostMapping("/{userId:\\d+}/disable")
    public ResultEntity disable(@PathVariable("userId") Integer userId) {
        return ResultEntity.success(userService.disableUserByID(userId));
    }

    @OperationLog("激活账号")
    @PostMapping("/{userId}/enable")
    public ResultEntity enable(@PathVariable("userId") Integer userId) {
        return ResultEntity.success(userService.enableUserByID(userId));
    }

    @OperationLog("删除账号")
    @DeleteMapping("/{userId}")
    public ResultEntity delete(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
        return ResultEntity.success();
    }

    @GetMapping("/{userId}/reset")
    public String resetPassword(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("userId", userId);
        return "user/user-reset-pwd";
    }

    @OperationLog("重置密码")
    @PostMapping("/{userId}/reset")
    public ResultEntity resetPassword(@PathVariable("userId") Integer userId, String password) {
        userService.updatePasswordByUserId(userId, password);
        return ResultEntity.success();
    }
}