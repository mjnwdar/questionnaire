package com.zzq.system.task;

import com.zzq.system.core.LogisticRegression;
import java.io.File;

/**
 * @author Sherlock
 * @copyright freeman
 * @since 2020/7/6 18:16
 */
public class AnalysisTask implements Runnable {
    private File path;
    public AnalysisTask(File path) {
        this.path = path;
    }
    @Override
    public void run() {
        if (this.path.exists()) {
            LogisticRegression.train(this.path.getAbsolutePath());
        }
    }
}
