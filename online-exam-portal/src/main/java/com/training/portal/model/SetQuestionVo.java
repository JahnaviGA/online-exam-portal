package com.training.portal.model;

import com.training.portal.utils.QuestionType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Data
public class SetQuestionVo {

	private Long questionId;
	
	private Boolean IsAnswered;
	
	private String trainerName;
	
	private String location;
	
	private String technology;
	
	private String questionDescription;
	
	private String subject;
	
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private String answerKey;

	private Collection<Answer> answerVo;

	private LocalDateTime time;
	
}
