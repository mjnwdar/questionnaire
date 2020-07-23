package com.zzq.common.aop;

import com.zzq.system.service.ShiroService;
import javax.annotation.Resource;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 更新过滤器链
 */
@Aspect
@Component
public class RefreshFilterChainAspect {

    @Resource
    private ShiroService shiroService;

    @Pointcut("@annotation(com.zzq.common.annotation.RefreshFilterChain)")
    public void updateFilterChain() {}

    @AfterReturning("updateFilterChain()")
    public void doAfter() {
        shiroService.updateFilterChain();
    }

}