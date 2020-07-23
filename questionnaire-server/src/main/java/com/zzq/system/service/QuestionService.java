package com.zzq.system.service;

import com.zzq.system.mapper.QuestionMapper;
import com.zzq.system.model.Question;
import com.zzq.system.model.QuestionItem;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public Boolean putQuestion(Question question) {
        isValidSurveyByCount(question, 10);
        questionMapper.save(question);
        return null;
    }

    public boolean isValidSurveyByCount(final Question question, final int limit) {
        boolean isValid = true;
        final List<QuestionItem> questions = question.getQuestions();
        String tempAnswer = "";
        int sameAnswerCount = 1;
        for (QuestionItem questionItem : questions) {
            if (!tempAnswer.equals(questionItem.getAnswer())) {
                tempAnswer = questionItem.getAnswer();
            } else {
                if (++sameAnswerCount >= limit) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    public boolean isValidSurveyByPercent(final Question question, final float percent) {
        boolean isValid = true;
        final List<QuestionItem> questions = question.getQuestions();
        float totalCount = questions.size();
        final Map<String, Long> collect = questions.stream().collect(
            Collectors.groupingBy(QuestionItem::getAnswer,
                Collectors.counting()));
        for (Map.Entry<String, Long> entry : collect.entrySet()) {
            Long count = entry.getValue();
            float itemPercent = count / totalCount;
            if (itemPercent >= percent) {
                isValid = false;
            }
        }
        return isValid;
    }
}
