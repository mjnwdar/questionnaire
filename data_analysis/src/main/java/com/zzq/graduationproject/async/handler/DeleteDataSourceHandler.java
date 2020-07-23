package com.zzq.graduationproject.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzq.graduationproject.async.EventHandler;
import com.zzq.graduationproject.async.EventModel;
import com.zzq.graduationproject.async.EventType;
import com.zzq.graduationproject.dao.DataSourceDao;
import com.zzq.graduationproject.model.DataSource;
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
public class DeleteDataSourceHandler implements EventHandler {

    @Autowired
    private DataSourceDao dataSourceDao;

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

        // 1. 从事件实体中获取数据源信息
        String dataSourceStr = eventModel.getExts("dataSource");
        DataSource dataSource = JSONObject.parseObject(dataSourceStr, DataSource.class);

        String type = dataSource.getSourceType();
        type = type.toLowerCase();

        // 2. 跟据数据源的类型删除存储信息
        boolean isOk = false;
        try {
            switch (type) {
                case "hdfs":
                    isOk = hdfsUtil.delete(dataSource.getHdfsPath(), true);
                    break;
                case "hive":
                    String deleteCommand = "drop table " + clusterHiveDatabase + "." + dataSource.getHiveTable() + ";";
                    String command = "source /etc/profile && hive -S -e \"" + deleteCommand + "\"";
                    String result = remoteUtil.execute(command);
                    System.out.println(result);
                  if (!result.contains("Exception")) {
                    isOk = true;
                  }
                    break;
            }

            // 3. 删除数据库中的数据源信息记录
            if (isOk) {
                dataSourceDao.deleteDataSource(dataSource.getId());
                message.setContent("【" + dataSource.getSourceName() + "】 删除成功");
            } else {
                message.setContent("【" + dataSource.getSourceName() + "】 删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.setContent("【" + dataSource.getSourceName() + "】 删除失败：" + e.getMessage());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.DELETE_DATASOURCE);
    }
}
