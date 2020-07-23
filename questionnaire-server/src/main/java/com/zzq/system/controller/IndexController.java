package com.zzq.system.controller;

import com.zzq.common.annotation.OperationLog;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.service.LoginLogService;
import com.zzq.system.service.MenuService;
import com.zzq.system.service.RoleService;
import com.zzq.system.service.SysLogService;
import com.zzq.system.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
public class IndexController {

    @Resource
    private MenuService menuService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private SysLogService sysLogService;

    @OperationLog("访问我的桌面")
    @GetMapping("/welcome")
    public ResultEntity<Map<String, Integer>> welcome() {
        int userCount = userService.count();
        int roleCount = roleService.count();
        int menuCount = menuService.count();
        int loginLogCount = loginLogService.count();
        int sysLogCount = sysLogService.count();

        Map<String, Integer> resultMap = new WeakHashMap<>(5);
        resultMap.put("userCount", userCount);
        resultMap.put("roleCount", roleCount);
        resultMap.put("menuCount", menuCount);
        resultMap.put("loginLogCount", loginLogCount);
        resultMap.put("sysLogCount", sysLogCount);
        return ResultEntity.success(resultMap);
    }

    @OperationLog("查看近七日登录统计图")
    @GetMapping("/weekLoginCount")
    public ResultEntity<List<Integer>> recentlyWeekLoginCount() {
        return ResultEntity.success(loginLogService.recentlyWeekLoginCount());
    }
}
