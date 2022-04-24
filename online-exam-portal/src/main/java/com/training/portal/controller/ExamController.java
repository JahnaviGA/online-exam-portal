package com.training.portal.controller;

import com.training.portal.model.*;
import com.training.portal.request_response.AnswerRequest;
import com.training.portal.request_response.RequestExam;
import com.training.portal.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@RestController
@RequestMapping("/online-training-portal-application")
@Validated
@Slf4j
public class ExamController {

    @Autowired
    private ExamService portalService;


    /**
     * Get all the questions which is asked for examination
     * [Secured by Spring security]
     *
     * @return
     */
    @GetMapping("/examination/all-questions")
    public ResponseEntity<Collection<Question>> loadQuestions(@RequestParam String traineeName) {
        log.info("Questions from repo");
        return ResponseEntity.status(HttpStatus.OK).body(portalService.loadQuestions(traineeName));
    }

    /**
     * Api which is start the examination and by trainees
     *
     * @param request
     * @return
     */
    @PostMapping("/examination/question")
    public ResponseEntity<String> questions(@RequestBody AnswerRequest request) {
        log.warn("Examination process started");
        return ResponseEntity.status(HttpStatus.OK).body(portalService.questions(request));
    }

    /**
     * Load the option answer by questionId to answer the question
     *
     * @param questionId
     * @return
     */
    @PostMapping("/examination/load-options")
    public ResponseEntity<Collection<Answer>> loadOptions(@RequestParam Long questionId, @RequestParam @NotBlank(message = "Enter your name") String traineeName) {
        log.warn("Options loaded apis started");
        return ResponseEntity.status(HttpStatus.OK).body(portalService.loadOptions(questionId, traineeName));
    }

    @PostMapping("/examination/permission")
    public ResponseEntity<String> permitForExam(@RequestBody RequestExam requestedTrainee) {
        log.warn("Request to attend the examination...");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(portalService.permitForExam(requestedTrainee));
    }
}
