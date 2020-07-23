package com.zzq.system.model;

import java.util.List;
import lombok.Data;

/**
 * @author Sherlock
 */
@Data
public class QuestionItem {

    /**
     * 问题索引
     */
    private Integer questionIndex;
    /**
     * 问题内容
     */
    private String questionContent;
    /**
     * 答案
     */
    private String answer;

    /**
     * 问题选项
     */
    private List<Option> questionOptions;
}
