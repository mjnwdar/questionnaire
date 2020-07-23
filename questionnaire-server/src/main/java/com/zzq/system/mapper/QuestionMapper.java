package com.zzq.system.mapper;

import com.zzq.system.model.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sherlock
 */
@Mapper
public interface QuestionMapper {

    void save(Question question);
}
