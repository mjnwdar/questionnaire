package com.zzq.graduationproject.async;

/**
 * @author zzq
 */
public enum EventType {

    REGISTER_INIT_HDFS(1), // 注册用户
    USER_DELETE(2), // 删除用户
    FILE_UPLOAD_HDFS(3), // 本地文件上传至HDFS
    FILE_UPLOAD_HIVE(4), // 本地文件上传至Hive
    MYSQL_IMPORT_HDFS(5), // 数据库拉取至HDFS
    MYSQL_IMPORT_HIVE(6), // 数据库拉取至Hive
    DELETE_DATASOURCE(7), // 删除数据源
    START_DATA_FLOW(8), // 启动数据流程
    DELETE_DATA_FLOW(9); // 删除数据流程


    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
