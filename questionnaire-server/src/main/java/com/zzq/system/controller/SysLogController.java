package com.zzq.system.controller;

import com.github.pagehelper.PageInfo;
import com.zzq.common.annotation.OperationLog;
import com.zzq.common.util.PageResultBean;
import com.zzq.system.model.SysLog;
import com.zzq.system.service.SysLogService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/sys")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @OperationLog("查看操作日志")
    @GetMapping("/list")
    public PageResultBean<SysLog> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10")int limit) {
        List<SysLog> loginLogs = sysLogService.selectAll(page, limit);
        PageInfo<SysLog> rolePageInfo = new PageInfo<>(loginLogs);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }

}
