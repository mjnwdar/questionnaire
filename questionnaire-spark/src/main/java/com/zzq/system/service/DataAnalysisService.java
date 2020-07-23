package com.zzq.system.service;

import com.zzq.system.core.LogisticRegression;
import com.zzq.system.core.MakeCsvFile;
import com.zzq.system.mapper.QuestionMapper;
import com.zzq.system.model.Question;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sherlock
 */
@Slf4j
@Service
public class DataAnalysisService {

    @Autowired
    private QuestionService questionService;

    public String startDataAnalysis(String date) {
        List list = questionService.queryData(date);
        try {
            final String filePath = MakeCsvFile.makeWithDbData(list);
            LogisticRegression.train(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
