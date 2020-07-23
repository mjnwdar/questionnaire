package com.zzq.system.service;

import com.zzq.system.mapper.QuestionMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Sherlock
 */
@Slf4j
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public List queryData(String date) {
        return questionMapper.queryData(date);
    }
}
