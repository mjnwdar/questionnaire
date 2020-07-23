package com.zzq.system.controller;

import com.zzq.common.annotation.OperationLog;
import com.zzq.common.constants.AuthcTypeEnum;
import com.zzq.common.shiro.OAuth2Helper;
import com.zzq.common.util.ResultEntity;
import com.zzq.common.util.ShiroUtil;
import com.zzq.system.model.UserAuths;
import com.zzq.system.model.vo.OAuth2VO;
import com.zzq.system.service.UserAuthsService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth2")
public class OAuth2Controller {

    @Resource
    private OAuth2Helper oAuth2Helper;

    @Resource
    private UserAuthsService userAuthsService;

    /**
     * 生成 Github 授权地址
     */
    @OperationLog("Github OAuth2 登录")
    @GetMapping("/render/github")
    public ResultEntity renderGithubAuth(HttpServletResponse response) {
        AuthRequest authRequest = oAuth2Helper.getAuthRequest(AuthcTypeEnum.GITHUB);
        return ResultEntity.successData(authRequest.authorize());
    }

    /**
     * 生成 Gitee 授权地址
     */
    @OperationLog("Gitee OAuth2 登录")
    @GetMapping("/render/gitee")
    public ResultEntity renderGiteeAuth(HttpServletResponse response) {
        AuthRequest authRequest = oAuth2Helper.getAuthRequest(AuthcTypeEnum.GITEE);
        return ResultEntity.successData(authRequest.authorize());
    }

    @OperationLog("获取账号绑定信息")
    @GetMapping("/list")
    public ResultEntity list() {

        List<OAuth2VO> authsList = new ArrayList<>();

        for (AuthcTypeEnum type : AuthcTypeEnum.values()) {
            UserAuths auth = userAuthsService.selectOneByIdentityTypeAndUserId(type, ShiroUtil.getCurrentUser().getUserId());

            OAuth2VO oAuth2VO = new OAuth2VO();

            oAuth2VO.setType(type.name());
            oAuth2VO.setDescription(type.getDescription());
            oAuth2VO.setStatus(auth == null ?  "unbind" : "bind");
            oAuth2VO.setUsername(auth == null ? "" : auth.getIdentifier());
            authsList.add(oAuth2VO);
        }

        return ResultEntity.success(authsList);
    }

    /**
     * 取消授权
     */
    @OperationLog("取消账号绑定")
    @GetMapping("/revoke/{provider}")
    public Object revokeAuth(@PathVariable("provider") AuthcTypeEnum provider) {
        UserAuths userAuths = userAuthsService.selectOneByIdentityTypeAndUserId(provider, ShiroUtil.getCurrentUser().getUserId());

        if (userAuths == null) {
            return ResultEntity.error("已经是未绑定状态!");
        }

        userAuthsService.deleteByPrimaryKey(userAuths.getId());
        return ResultEntity.success();
    }

    @GetMapping("/success")
    public String success() {
        return "oauth2/authorize-success";
    }

    @GetMapping("/error")
    public String error() {
        return "oauth2/authorize-error";
    }

}
