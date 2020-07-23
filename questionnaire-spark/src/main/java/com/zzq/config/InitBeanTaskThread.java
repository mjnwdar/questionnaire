package com.zzq.config;

import com.zzq.system.core.MakeCsvFile;
import com.zzq.system.task.AnalysisTask;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sherlock
 * @copyright freeman
 * @since 2020/7/6 22:01
 */
@Component
public class InitBeanTaskThread {


    @Autowired
    private ScheduledThreadPoolExecutor scheduledExecutor;

    @PostConstruct
    public void init() {
        AnalysisTask analysisTask = null;
        try {
            analysisTask = new AnalysisTask(MakeCsvFile.getTestFilePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scheduledExecutor.scheduleAtFixedRate(analysisTask, 0, 12, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        scheduledExecutor.shutdown();
    }
}
