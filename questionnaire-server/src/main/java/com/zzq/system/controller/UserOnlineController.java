package com.zzq.system.controller;

import com.zzq.common.util.PageResultBean;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.UserOnline;
import com.zzq.system.service.UserOnlineService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
@RequestMapping("/online")
public class UserOnlineController {
    @Resource
    private UserOnlineService userOnlineService;

    @GetMapping("/list")
    public PageResultBean<UserOnline> online() {
        List<UserOnline> list = userOnlineService.list();
        return new PageResultBean<>(list.size(), list);
    }

    @PostMapping("/kickout")
    public ResultEntity forceLogout(String sessionId) {
        userOnlineService.forceLogout(sessionId);
        return ResultEntity.success();
    }
}
