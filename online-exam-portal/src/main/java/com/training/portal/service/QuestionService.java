package com.training.portal.service;

import com.training.portal.exception.BusinessException;
import com.training.portal.exception.ErrorDetails;
import com.training.portal.model.*;
import com.training.portal.repository.AnswerRepo;
import com.training.portal.repository.QuestionRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class QuestionService {

	@Autowired
	private QuestionRepo questionRepo;
	
	@Autowired
	private AnswerRepo answerRepo;

	public  Question  addQuestion(Question question) {
		question.setDate(LocalDate.now());
		question.setIsAnswered(false);
		question.setSubjectId(generateSubjectId(question.getSubject()));
		question.setAnswerKey(answerKeyId(question));
		Question returnReponse = questionRepo.save(question);
		log.info("Question saved to repo");
		return returnReponse;
	}


    public Page<Question> allQuestionsWithAnswer(Integer pageNo, Integer offset) {
		Pageable paging = PageRequest.of(pageNo, offset ==null ? 10 :offset);
		log.info("Paging request to access the question for page - {},size-{}",paging.getPageNumber(),paging.getPageNumber());
		Page<Question> questions =  questionRepo.findAll(paging);
		questions.stream().sorted(Comparator.comparing(Question::getId));
		log.info("Sorted and filtered question final response from repo");
		return questions;
    }

	private String generateSubjectId(String subject){
		return subject.substring(0, 3) +(questionRepo.count()+1);
	}

	private String answerKeyId(Question qn){
		return "A"+questionRepo.count()+1+qn.getSubject();
	}

    public String removeQuestion(Long id) {
		try{
			questionRepo.deleteById(id);
			return "Question removed";
		}catch (NullPointerException | HttpClientErrorException.NotFound ex){
			throw new BusinessException(ErrorDetails.RESOURCE_NOT_FOUND.getMessage());
		}
	}

	public SetQuestionVo setQuestionDetails(SetQuestionVo question) {
		Trainer setQuestionTrainer = new Trainer();
		SetQuestionVo response = new SetQuestionVo();
		//Question setQuestion = new Question();
		Question findQuestion = questionRepo.findById(question.getQuestionId()).get();
		if(ObjectUtils.isNotEmpty(findQuestion))
			setQuestionTrainer.setName(question.getTrainerName());
		setQuestionTrainer.setLocation(question.getLocation());
		setQuestionTrainer.setTechnology(question.getTechnology());
		findQuestion.setDescription(findQuestion.getDescription());
		findQuestion.setSubject(question.getSubject());
		findQuestion.setQuestionType(findQuestion.getQuestionType());
		findQuestion.setSubjectId(findQuestion.getSubjectId());
		findQuestion.setDate(LocalDate.now());
		findQuestion.setIsAnswered(false);
		findQuestion.setAnswerKey(question.getAnswerKey());
		//	Answer setAnswers = new Answer();
		Collection<Answer> savedOptions = new ArrayList<>();
		Answer answerVo = new Answer();
		question.getAnswerVo().stream().forEach(answer ->{
			answer.setQuestionId(findQuestion.getId());
			answerVo.setAnswer(answer.getAnswer());
			answerVo.setIsCorrectAnswer(answer.getIsCorrectAnswer());
		});
		answerVo.setQuestionId(findQuestion.getId());
		savedOptions.addAll(question.getAnswerVo());
//		setAnswers.setQuestionId(findQuestion.getId());
//		setAnswers.setAnswer(answerVo.getAnswer());
//		setAnswers.setIsCorrectAnswer(answerVo.getIsCorrectAnswer());
		questionRepo.save(findQuestion);
		Collection<Answer>  options = answerRepo.saveAll(savedOptions);
		response.setQuestionId(findQuestion.getId());
		response.setTrainerName(setQuestionTrainer.getName());
		response.setLocation(setQuestionTrainer.getLocation());
		response.setTechnology(setQuestionTrainer.getTechnology());
		response.setSubject(findQuestion.getSubject());
		response.setQuestionDescription(findQuestion.getDescription());
		response.setAnswerKey(findQuestion.getAnswerKey());
		response.setQuestionType(findQuestion.getQuestionType());
		response.setAnswerVo(options);
		response.setTime(LocalDateTime.now());
		response.setIsAnswered(findQuestion.getIsAnswered());
		return response;
	}
}
