package com.zzq.system.controller;

import com.zzq.common.annotation.OperationLog;
import com.zzq.common.information.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
public class SystemController {

    @OperationLog("查看系统信息")
    @GetMapping("/system/index")
    public Server index() throws Exception {
        Server server = new Server();
        server.copyTo();
        return server;
    }
}
