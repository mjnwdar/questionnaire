package com.zzq.system.model;

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
}
