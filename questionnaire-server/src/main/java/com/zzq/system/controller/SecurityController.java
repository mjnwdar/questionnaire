package com.zzq.system.controller;

import com.zzq.common.util.ResultEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全相关 Controller
 */
@RestController
@RequestMapping("/security")
public class SecurityController {

    /**
     * 判断当前登录用户是否有某权限
     */
    @GetMapping("/hasPermission/{perms}")
    public ResultEntity hasPermission(@PathVariable("perms") String perms) {
        return ResultEntity.success(SecurityUtils.getSubject().isPermitted(perms));
    }

}
