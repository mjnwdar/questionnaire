package com.zzq.graduationproject.controller;

import com.zzq.graduationproject.model.HostHolder;
import com.zzq.graduationproject.model.Message;
import com.zzq.graduationproject.model.Page;
import com.zzq.graduationproject.model.Result;
import com.zzq.graduationproject.model.ResultCode;
import com.zzq.graduationproject.model.User;
import com.zzq.graduationproject.service.MessageService;
import com.zzq.graduationproject.utils.ResultUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zzq
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @GetMapping("/manager")
    public String manager(Model model,
        @RequestParam(value = "p", defaultValue = "1") int pageNum) {

        User user = hostHolder.getUser();

        model.containsAttribute("user");
//    model.addAttribute("user", user);

        model.addAttribute("page", "messageManager");

        int pageSize = 10;
        Page page = new Page(pageNum, pageSize, messageService.getMessageCount(user.getId()));
        List<Message> list = messageService.getMessageList(user.getId(),
            page.getOffset(), page.getPageSize());

        model.addAttribute("messages", list);
        model.addAttribute("pages", page);

        return "messageManager";
    }

    @PostMapping("/read")
    @ResponseBody
    public Result readTheMessage(Integer messageId) {

      if (messageId == null) {
        return ResultUtil.error(ResultCode.FAIL.getCode(), "缺失消息id");
      }

        messageService.updateReadMessage(messageId);

        return ResultUtil.success();
    }

}
