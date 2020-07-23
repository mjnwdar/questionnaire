package com.zzq.graduationproject.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zzq.graduationproject.async.EventHandler;
import com.zzq.graduationproject.async.EventModel;
import com.zzq.graduationproject.async.EventType;
import com.zzq.graduationproject.model.DataSource;
import com.zzq.graduationproject.model.DatabaseInfo;
import com.zzq.graduationproject.model.Message;
import com.zzq.graduationproject.service.DataSourceService;
import com.zzq.graduationproject.service.MessageService;
import com.zzq.graduationproject.utils.DbUtil;
import com.zzq.graduationproject.utils.RemoteUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzq
 */
@Service
public class MySQLImportHDFSHandler implements EventHandler {

    @Autowired
    private RemoteUtil remoteUtil;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message msg = new Message();
        msg.setUserId(eventModel.getActorId());
        String command = eventModel.getExts("command");
        String approach = eventModel.getExts("approach");
        DataSource dataSource = JSONObject.parseObject(eventModel.getExts("dataSource"), DataSource.class);

        try {
            String result = remoteUtil.execute(command);

            System.out.println("========= 【MySQLImportHDFSHandler】 执行结果 =========");
            System.out.println(result);

            if (result.contains("completed successfully")) {
                if (eventModel.getEventType() == EventType.MYSQL_IMPORT_HIVE && approach.equals("create")) {
                    DatabaseInfo databaseInfo = JSONObject.parseObject(eventModel.getExts("databaseInfo"), DatabaseInfo.class);
                    String url = "jdbc:mysql://" + databaseInfo.getAddress()
                        + ":" + databaseInfo.getPort() + "/" + databaseInfo.getDatabase()
                        + "?characterEncoding=" + databaseInfo.getCharset();

                    DbUtil dbUtil = new DbUtil(url, databaseInfo.getUsername(), databaseInfo.getPassword());
                    List<String> list = dbUtil.getTableInfo(databaseInfo.getTable());
                    StringBuilder cols = new StringBuilder();
                    for (String str : list) {
                        cols.append(str.split(",")[0]).append(",");
                    }
                    cols.deleteCharAt(cols.length() - 1);
                    dataSource.setTableColumn(cols.toString());
                }
                // 导入成功
                switch (approach) {
                    case "create":
                        dataSourceService.addDataSource(dataSource);
                        break;
                    case "append":
                    case "overwrite":
                        dataSource.setModifyData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataSourceService.updateDataSource(dataSource);
                        break;
                }

                if (eventModel.getEventType() == EventType.MYSQL_IMPORT_HDFS) {
                    msg.setContent("【" + dataSource.getSourceName() + "】MySQL导入HDFS成功");
                } else {
                    msg.setContent("【" + dataSource.getSourceName() + "】MySQL导入Hive成功");
                }

            } else {
                // 导入失败
                if (eventModel.getEventType() == EventType.MYSQL_IMPORT_HDFS) {
                    msg.setContent("【" + dataSource.getSourceName() + "】MySQL导入HDFS失败");
                } else {
                    msg.setContent("【" + dataSource.getSourceName() + "】MySQL导入Hive失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = new Message(eventModel.getActorId(), "【" + dataSource.getSourceName() + "】导入失败：" + e.getMessage());
        }

        messageService.addMessage(msg);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MYSQL_IMPORT_HDFS, EventType.MYSQL_IMPORT_HIVE);
    }
}
