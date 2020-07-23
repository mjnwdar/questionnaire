package com.zzq.graduationproject.async;


import com.alibaba.fastjson.JSONObject;
import com.zzq.graduationproject.utils.JedisUtil;
import com.zzq.graduationproject.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzq
 */
@Service
public class EventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 向事件队列发送事件
     *
     * @param eventModel
     * @return
     */
    public boolean emitEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisUtil.getEventQueueKey();

            jedisUtil.lpush(key, json);
            System.out.println("放入redis");
            return true;
        } catch (Exception e) {
            logger.error("出现异常" + e.getMessage());
        }

        return false;
    }
}
