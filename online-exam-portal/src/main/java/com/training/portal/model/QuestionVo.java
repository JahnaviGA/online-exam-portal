package com.training.portal.model;

import com.training.portal.utils.QuestionType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.Collection;

@Data
public class QuestionVo {

	private Long questionId;

	private String description;

	private String subjectId;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private LocalDate date;

	private Boolean IsAnswered;

}
