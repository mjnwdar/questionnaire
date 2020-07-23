package com.zzq.graduationproject.async;

import java.util.List;

/**
 * @author zzq
 */
public interface EventHandler {

    // 具体的处理操作
    void doHandle(EventModel eventModel);

    // 支持处理的事件类型
    List<EventType> getSupportEventTypes();
}
