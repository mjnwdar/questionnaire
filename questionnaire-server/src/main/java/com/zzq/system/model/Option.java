package com.zzq.system.model;

import lombok.Data;

/**
 * @author Sherlock
 */
@Data
public class Option {

    /**
     * 选项内容
     */
    private String content;
    /**
     * 选项得分
     */
    private Integer score;
    /**
     * 选项互斥
     */
    private String mutex;
}
