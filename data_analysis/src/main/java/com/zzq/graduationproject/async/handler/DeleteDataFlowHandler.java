package com.zzq.graduationproject.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzq.graduationproject.async.EventHandler;
import com.zzq.graduationproject.async.EventModel;
import com.zzq.graduationproject.async.EventType;
import com.zzq.graduationproject.dao.DataFlowDao;
import com.zzq.graduationproject.model.DataFlow;
import com.zzq.graduationproject.model.Message;
import com.zzq.graduationproject.service.MessageService;
import com.zzq.graduationproject.utils.HDFSUtil;
import com.zzq.graduationproject.utils.RemoteUtil;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zzq
 */
@Service
public class DeleteDataFlowHandler implements EventHandler {

    @Autowired
    private DataFlowDao dataFlowDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HDFSUtil hdfsUtil;

    @Autowired
    private RemoteUtil remoteUtil;

    @Value("${cluster.hive.database}")
    private String clusterHiveDatabase;


    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setUserId(eventModel.getActorId());

        // 1. 从事件实体中获取数据流程信息
        String dataFlowStr = eventModel.getExts("dataFlow");
        DataFlow dataFlow = JSONObject.parseObject(dataFlowStr, DataFlow.class);

        String type = dataFlow.getFlowType();
        type = type.toLowerCase();

        // 2. 根据数据流程的类型删除数据
        boolean isOk = false;
        try {
            switch (type) {
                case "mr":
                    isOk = hdfsUtil.delete(dataFlow.getResultPath(), true);
                    break;
                case "hql":
                    String deleteCommand = "drop table " + clusterHiveDatabase + "." + dataFlow.getResultTable() + ";";
                    String command = "source /etc/profile && hive -S -e \"" + deleteCommand + "\"";
                    String result = remoteUtil.execute(command);
                    System.out.println(result);
                  if (!result.contains("Exception")) {
                    isOk = true;
                  }
                    break;
            }

            // 3. 删除数据库中数据流程的记录
            if (isOk) {
                dataFlowDao.deleteDataFlow(dataFlow.getId());
                message.setContent("【" + dataFlow.getFlowName() + "】 删除成功");
            } else {
                message.setContent("【" + dataFlow.getFlowName() + "】 删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            message.setContent("【" + dataFlow.getFlowName() + "】 删除失败：" + e.getMessage());
        }

        // 4. 推送消息
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.DELETE_DATA_FLOW);
    }
}
