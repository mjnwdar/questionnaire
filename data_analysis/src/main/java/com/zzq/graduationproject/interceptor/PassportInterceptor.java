package com.zzq.graduationproject.interceptor;

import com.zzq.graduationproject.dao.LoginTokenDao;
import com.zzq.graduationproject.dao.UserDao;
import com.zzq.graduationproject.model.HostHolder;
import com.zzq.graduationproject.model.LoginToken;
import com.zzq.graduationproject.model.User;
import com.zzq.graduationproject.service.MessageService;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zzq
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);

    @Autowired
    LoginTokenDao loginTokenDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler)
        throws Exception {

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            LoginToken loginToken = loginTokenDao.getLoginTokenByToken(token);
            if (loginToken == null ||
                loginToken.getExpired().before(new Date()) ||
                loginToken.getStatus() == 1) {
                return true;
            }

            User user = userDao.getUserById(loginToken.getUserId());
            hostHolder.setUsers(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        ModelAndView modelAndView)
        throws Exception {

        if (modelAndView != null) {
            User user = hostHolder.getUser();
            modelAndView.addObject("user", user);
            if (user != null) {
                modelAndView.addObject("messageUnread", messageService.getUnreadMessageCount(hostHolder.getUser().getId()));
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex)
        throws Exception {

        hostHolder.clear();
    }
}
