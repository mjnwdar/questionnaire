package com.zzq.system.controller;

import com.github.pagehelper.PageInfo;
import com.zzq.common.annotation.OperationLog;
import com.zzq.common.util.PageResultBean;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.Role;
import com.zzq.system.service.RoleService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.ui.Model;
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
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @OperationLog("查询角色列表")
    @GetMapping("/list")
    public PageResultBean<Role> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10")int limit,
                                        Role roleQuery) {
        List<Role> roles = roleService.selectAll(page, limit, roleQuery);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roles);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }


    @OperationLog("新增角色")
    @PostMapping
    public ResultEntity add(Role role) {
        roleService.add(role);
        return ResultEntity.success();
    }

    @GetMapping("/{roleId}")
    public String update(@PathVariable("roleId") Integer roleId, Model model) {
        Role role = roleService.selectOne(roleId);
        model.addAttribute("role", role);
        return "role/role-add";
    }

    @OperationLog("修改角色")
    @PutMapping
    public ResultEntity update(Role role) {
        roleService.update(role);
        return ResultEntity.success();
    }


    @OperationLog("删除角色")
    @DeleteMapping("/{roleId}")
    public ResultEntity delete(@PathVariable("roleId") Integer roleId) {
        roleService.delete(roleId);
        return ResultEntity.success();
    }

    @OperationLog("为角色授予菜单")
    @PostMapping("/{roleId}/grant/menu")
    public ResultEntity grantMenu(@PathVariable("roleId") Integer roleId, @RequestParam(value = "menuIds[]", required = false) Integer[] menuIds) {
        roleService.grantMenu(roleId, menuIds);
        return ResultEntity.success();
    }


    @OperationLog("为角色授予操作权限")
    @PostMapping("/{roleId}/grant/operator")
    public ResultEntity grantOperator(@PathVariable("roleId") Integer roleId, @RequestParam(value = "operatorIds[]", required = false) Integer[] operatorIds) {
        roleService.grantOperator(roleId, operatorIds);
        return ResultEntity.success();
    }

    /**
     * 获取角色拥有的菜单
     */
    @GetMapping("/{roleId}/own/menu")
    public ResultEntity getRoleOwnMenu(@PathVariable("roleId") Integer roleId) {
        return ResultEntity.success(roleService.getMenusByRoleId(roleId));
    }

    /**
     * 获取角色拥有的操作权限
     */
    @GetMapping("/{roleId}/own/operator")
    public ResultEntity getRoleOwnOperator(@PathVariable("roleId") Integer roleId) {
        Integer[] operatorIds = roleService.getOperatorsByRoleId(roleId);
        for (int i = 0; i < operatorIds.length; i++) {
            operatorIds[i] = operatorIds[i] + 10000;
        }
        return ResultEntity.success(operatorIds);
    }
}
