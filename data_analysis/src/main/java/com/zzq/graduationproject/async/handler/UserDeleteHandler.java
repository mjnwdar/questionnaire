package com.zzq.graduationproject.async.handler;

import com.zzq.graduationproject.async.EventHandler;
import com.zzq.graduationproject.async.EventModel;
import com.zzq.graduationproject.async.EventType;
import com.zzq.graduationproject.utils.RemoteUtil;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用户删除 - 事件处理器
 *
 * @author zzq
 */
@Service
public class UserDeleteHandler implements EventHandler {

    private Logger logger = LoggerFactory.getLogger(UserDeleteHandler.class);

    @Autowired
    private RemoteUtil remoteUtil;

    @Value("${cluster.user.path}")
    private String userHDFSPath;

    @Override
    public void doHandle(EventModel eventModel) {

        String username = eventModel.getExts("username");
        String command = "source /etc/profile && " +
            "hadoop fs -rm -r -f " + userHDFSPath + "/" + username;

        String msg = remoteUtil.execute(command);
        logger.info(msg);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.USER_DELETE);
    }
}
