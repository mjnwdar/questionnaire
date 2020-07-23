package com.zzq.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sherlock
 */
@Mapper
public interface QuestionMapper {
    List queryData(String date);
}
