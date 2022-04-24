package com.training.portal.controller;

import com.training.portal.model.*;
import com.training.portal.request_response.LoginRequest;
import com.training.portal.request_response.LoginResponse;
import com.training.portal.request_response.SignUpResponse;
import com.training.portal.service.UserDetailsServiceImpl;
import com.training.portal.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/online-training-portal-application")
@Validated
@Slf4j
public class AuthenticationController {

    @Autowired
    UserDetailsServiceImpl authenticationService;

    /**
     * Authentication gateway named login using Spring core and web config security
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        log.info("Login security gateway called by - {}",request.getEmailId());
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request.getEmailId(), request.getPassword()));
    }

    /**
     * Registration api for trainer who has admin role to do authenticated  matters in this application
     *
     * @param requestTrainer
     * @return
     */
    @PostMapping("/trainer-signup")
    public  ResponseEntity<SignUpResponse> signUpTrainer(@Valid @RequestBody Trainer requestTrainer) {
        log.info("Trainer registration service called by - {}",requestTrainer.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.trainerSignUp(requestTrainer));
    }

    /**
     * Get all trainer by their names in Sort response
     * [Secured by Spring security]
     * @param name
     * @return
     */
    @Secured(value = Constants.ROLE_AD)
    @GetMapping("/trainer/{name}")
    public ResponseEntity<Collection<Trainer>> getTrainerObj(@PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.getTrainerByName(name));
    }

    /**
     * SignUp for trainee who are going to attend the online examinations
     *
     * @param requestTrainee
     * @return
     */
    @PostMapping("/trainee-signup")
    public  ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody Trainee requestTrainee) {
        log.info("Trainee registration service called by - {}",requestTrainee.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.traineeSignUp(requestTrainee));
    }
}

