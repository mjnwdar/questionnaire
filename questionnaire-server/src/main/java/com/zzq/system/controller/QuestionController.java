package com.zzq.system.controller;

import com.zzq.common.annotation.RefreshFilterChain;
import com.zzq.common.util.ResultEntity;
import com.zzq.system.model.Question;
import com.zzq.system.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sherlock
 */
@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RefreshFilterChain
    @PutMapping
    public ResultEntity putQuestionList(Question question) {
        Boolean success = questionService.putQuestion(question);
        return ResultEntity.success(success);
    }
}
