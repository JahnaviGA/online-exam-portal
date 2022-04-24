package com.training.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answerD")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long questionId;
	
	@Column
	private String answer;

	@Column
	private Boolean isCorrectAnswer;
}
