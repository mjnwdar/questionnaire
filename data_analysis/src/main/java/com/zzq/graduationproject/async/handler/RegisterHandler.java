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
 * 注册时触发事件处理器 分配HDFS目录
 *
 * @author zzq
 */
@Service
public class RegisterHandler implements EventHandler {

    private Logger logger = LoggerFactory.getLogger(RegisterHandler.class);

    @Autowired
    private RemoteUtil remoteUtil;

    @Value("${cluster.user.path}")
    private String userHDFSPath;

    @Override
    public void doHandle(EventModel eventModel) {

        String username = eventModel.getExts("username");
        String command = "source /etc/profile && " +
            "hadoop fs -mkdir -p " + userHDFSPath + "/" + username + "/hdfs && " +
            "hadoop fs -mkdir -p " + userHDFSPath + "/" + username + "/hive && " +
            "hadoop fs -mkdir -p " + userHDFSPath + "/" + username + "/result";

        remoteUtil.execute(command);
        logger.info("【用户注册】HDFS初始化完毕");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.REGISTER_INIT_HDFS);
    }
}
