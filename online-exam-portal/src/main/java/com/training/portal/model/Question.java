package com.training.portal.model;

import com.training.portal.utils.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questionD")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String description;
	
	private String subjectId;

	private String subject;
	
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;
	
	private LocalDate date;
	
	private Boolean IsAnswered;

	private String answerKey;

}
