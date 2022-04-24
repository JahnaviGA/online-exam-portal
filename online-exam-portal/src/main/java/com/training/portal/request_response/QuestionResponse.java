package com.training.portal.request_response;

import com.training.portal.model.Answer;
import com.training.portal.utils.QuestionType;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class QuestionResponse {

    private Long questionId;

    private Boolean IsAnswered;

    private String questionDescription;

    private String subject;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private Collection<Answer> answerVo;

    private LocalDateTime time;
}
