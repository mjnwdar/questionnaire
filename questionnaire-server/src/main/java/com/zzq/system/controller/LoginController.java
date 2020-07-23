package com.zzq.system.controller;

import com.github.pagehelper.PageInfo;
import com.zzq.common.annotation.OperationLog;
import com.zzq.common.exception.CaptchaIncorrectException;
import com.zzq.common.shiro.ShiroActionProperties;
import com.zzq.common.util.CaptchaUtil;
import com.zzq.common.util.CaptchaUtil.Captcha;
import com.zzq.common.util.IPUtils;
import com.zzq.common.util.PageResultBean;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.LoginLog;
import com.zzq.system.model.Menu;
import com.zzq.system.model.User;
import com.zzq.system.service.LoginLogService;
import com.zzq.system.service.MenuService;
import com.zzq.system.service.UserService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
public class LoginController {

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @Resource
    private ShiroActionProperties shiroActionProperties;

    @OperationLog("登录")
    @PostMapping("/login")
    public ResultEntity<List<Menu>> login(User user, @RequestParam(value = "captcha", required = false) String captcha) {
        Subject subject = SecurityUtils.getSubject();

        // 如果开启了登录校验
        if (shiroActionProperties.getLoginVerify()) {
            String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
            if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
                throw new CaptchaIncorrectException();
            }
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        userService.updateLastLoginTimeByUsername(user.getUsername());
        if (shiroActionProperties.getLogLogin()) {
            loginLogService.addLog(user.getUsername(), subject.isAuthenticated(), IPUtils.getIpAddr());
        }
        List<Menu> menuList = menuService.selectCurrentUserMenuTree();
        return ResultEntity.success("登录成功", menuList);
    }

    @OperationLog("注销")
    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        Captcha captcha = CaptchaUtil.createCaptcha(140, 38, 4, 10, 30);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("captcha", captcha.getCode());

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(captcha.getImage(), "png", os);
    }

    @OperationLog("查看登录日志")
    @GetMapping("/log/login/list")
    @ResponseBody
    public PageResultBean<LoginLog> getList(@RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<LoginLog> loginLogs = loginLogService.selectAll(page, limit);
        PageInfo<LoginLog> rolePageInfo = new PageInfo<>(loginLogs);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }
}
