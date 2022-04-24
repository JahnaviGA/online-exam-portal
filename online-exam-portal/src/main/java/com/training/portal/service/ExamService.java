package com.training.portal.service;

import com.training.portal.exception.BusinessException;
import com.training.portal.exception.ErrorDetails;
import com.training.portal.model.*;
import com.training.portal.repository.AnswerRepo;
import com.training.portal.repository.QuestionRepo;
import com.training.portal.repository.TraineeRepo;
import com.training.portal.request_response.AnswerRequest;
import com.training.portal.request_response.RequestExam;
import com.training.portal.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamService {

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private AnswerRepo answerRepo;

	@Autowired
	private TraineeRepo traineeRepo;

	public Collection<Question> loadQuestions(String traineeName) {
		Trainee existTrainee = traineeRepo.findByName(traineeName);
		log.info("Trainee found.....");
		if(!existTrainee.getIsAllowedForExam()){
			log.error("trainee didn't have permission to attend the exam");
			throw new BusinessException(ErrorDetails.DONT_ALLOW_FOR_EXAM);
		}
		Collection<Question> questions = questionRepo.findAll().stream().filter(qn -> qn.getIsAnswered() == false)
			.sorted().collect(Collectors.toList());
		log.info("Unanswered question from repo");
		return questions;
	}

	public String questions(AnswerRequest answer) {
		Answer findCorrectAnswer = answerRepo.findById(answer.getAnswerId()).get();
		if(answer.getQuestionId().equals(findCorrectAnswer.getQuestionId())){
			if(findCorrectAnswer.getIsCorrectAnswer()){
				return Constants.RIGHT_ANSWER;
			}
		}
		return Constants.WRONG_ANSWER;
	}

	public Collection<Answer> loadOptions(Long questionId,String traineeName) {
		if(StringUtils.isNotEmpty(traineeName)){
			Trainee existTrainee = traineeRepo.findByName(traineeName);
			log.info("Trainee found.....");
			if(!existTrainee.getIsAllowedForExam()){
				log.error("trainee didn't have permission to attend the exam");
				throw new BusinessException(ErrorDetails.DONT_ALLOW_FOR_EXAM);
			}
		}
		Answer findOne = new Answer();
		findOne.setQuestionId(questionId);
		Example<Answer> id = Example.of(findOne);
		answerRepo.findAll(id).forEach(answer -> {
			answer.setIsCorrectAnswer(Boolean.valueOf(StandardCharsets.UTF_8.encode(answer.getIsCorrectAnswer().toString()).toString()));
		});
		return answerRepo.findAll(id);
	}

	public String permitForExam(RequestExam requestedTrainee) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Trainee existTrainee = traineeRepo.findByName(requestedTrainee.getTraineeName());
		if(ObjectUtils.isNotEmpty(existTrainee)){
			if(requestedTrainee.getUserName().equalsIgnoreCase(existTrainee.getUserName())){
				if(passwordEncoder.matches(requestedTrainee.getPassword(),existTrainee.getPassword())){
					existTrainee.setIsAllowedForExam(true);
					traineeRepo.save(existTrainee);
					log.info("Got permission to attend the exam");
					return  requestedTrainee.getTraineeName() + "- Trainee Allowed to attend the Exam";
				}else{
					log.error("Passwords didn't matches");
					throw new BusinessException(ErrorDetails.PASSWORD_DIDNT_MATCHED);
				}
			}
		}
		return "Oops....!!Something went wrong";
	}
}
