package com.zzq.system.model;

import java.util.List;
import lombok.Data;

/**
 * @author Sherlock
 */
@Data
public class Question {

    private Integer questionId;
    /**
     * 答卷题目
     */
    private String questionName;
    /**
     * 问卷时间
     */
    private String createDate;
    /**
     * 试题
     */
    private List<QuestionItem> questions;
}
