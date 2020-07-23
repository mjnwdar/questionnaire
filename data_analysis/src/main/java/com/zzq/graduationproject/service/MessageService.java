package com.zzq.graduationproject.service;

import com.zzq.graduationproject.dao.MessageDao;
import com.zzq.graduationproject.model.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * @author zzq
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    /**
     * 添加消息
     *
     * @param message
     */
    public void addMessage(Message message) {

        // 过滤HTML标签
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));

        messageDao.addMessage(message);
    }

    /**
     * 获取聊天列表
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getMessageList(int userId, int offset, int limit) {

        return messageDao.getMessageList(userId, offset, limit);
    }

    public int getMessageCount(int userId) {

        return messageDao.getMessageCount(userId);
    }

    /**
     * 查询未读消息
     *
     * @param userId
     * @return
     */
    public int getUnreadMessageCount(int userId) {

        return messageDao.getUnreadMessageCount(userId);
    }

    /**
     * 标记消息已读
     *
     * @param id
     * @return
     */
    public void updateReadMessage(int id) {

        messageDao.updateReadMessage(id);
    }


}
