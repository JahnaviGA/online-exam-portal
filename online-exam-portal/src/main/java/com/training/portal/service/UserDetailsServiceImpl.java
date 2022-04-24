package com.training.portal.service;

import com.training.portal.exception.BusinessException;
import com.training.portal.exception.ErrorDetails;
import com.training.portal.request_response.LoginResponse;
import com.training.portal.request_response.SignUpResponse;
import com.training.portal.model.Trainee;
import com.training.portal.model.Trainer;
import com.training.portal.repository.TraineeRepo;
import com.training.portal.repository.TrainerRepo;
import com.training.portal.security.JwtTokenUtil;
import com.training.portal.utils.Constants;
import com.training.portal.utils.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TrainerRepo trainerRepo;
    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        Trainer trainer = this.getTrainer(emailId);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin")) ;
        return new User(trainer.getEmail(), trainer.getPassword(), authorities);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info("User authenticated");
        } catch (DisabledException e) {
            log.error("Error occured from security - {} ",e.toString());
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.error("Error occured from security by credentials - {} ",e.toString());
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    public LoginResponse login(String emailId, String password) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Trainer user = this.getTrainer(emailId);
        if (passwordEncoder.matches(password, user.getPassword())) {
            authenticate(emailId, password);
            return  getLoginResponse(user);
        }
        log.error("Error occured from user repo");
        throw new BusinessException(ErrorDetails.USER_NOT_FOUND);
    }

    private LoginResponse getLoginResponse(Trainer user) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(Constants.ROLE));
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("role", Constants.ROLE);
            final String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(), authorities),
                    claims );
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUserName(user.getName());
            response.setEmailId(user.getEmail());
            response.setRole(Role.TRAINER.getRole());
            return response;
        }

    public Trainer getTrainer(String emailId) {
        try {
            Optional<Trainer> existingUser = trainerRepo.findByEmail(emailId);
            if (existingUser.isPresent()) {
                return existingUser.get();
            }
            throw new BusinessException(ErrorDetails.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new BusinessException(ErrorDetails.USER_NOT_FOUND);
        }
    }
    public SignUpResponse trainerSignUp(Trainer requestTrainer) {
            Optional<Trainer> userRequest = trainerRepo.findByEmail(requestTrainer.getEmail());
            if (userRequest.isPresent()) {
                log.error("Already exist record from trainer repo - {} ",userRequest.toString());
                throw new BusinessException(ErrorDetails.USER_EXIST);
            }
            // Encoding password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Trainer user = new Trainer();
            user.setName(requestTrainer.getName());
            user.setUserName(requestTrainer.getUserName());
            user.setContact(requestTrainer.getContact());
            user.setEmail(requestTrainer.getEmail());
            user.setTechnology(requestTrainer.getTechnology());
            user.setLocation(requestTrainer.getLocation());
            user.setPassword(passwordEncoder.encode(requestTrainer.getPassword()));
            user.setRole(Role.TRAINER);
            trainerRepo.save(user);
            log.info("Trainer saved to Db successfully - {} ",user.toString());
            return  signUpResponseForTrainer(user);
        }
    private SignUpResponse signUpResponseForTrainer(Trainer  user) {
        return new SignUpResponse(user.getEmail(), user.getName(), Constants.REGISTERED_SUCCESSFULLY,
                user.getRole().equals(Role.TRAINER) ? Role.TRAINER : Role.TRAINEE);
    }

    public SignUpResponse traineeSignUp(Trainee requestTrainee) {
        // Encoding password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Trainee user = new Trainee();
        user.setName(requestTrainee.getName());
        user.setContact(requestTrainee.getContact());
        user.setDomain(requestTrainee.getDomain());
        user.setEducation(requestTrainee.getEducation());
        user.setLocation(requestTrainee.getLocation());
        user.setPassOutYear(requestTrainee.getPassOutYear());
        user.setTechnology(requestTrainee.getTechnology());
        user.setUserName(requestTrainee.getUserName());
        user.setPassword(passwordEncoder.encode(requestTrainee.getPassword()));
        user.setTrainerUserName(requestTrainee.getTrainerUserName());
        user.setRole(Role.TRAINEE);
        user.setIsAllowedForExam(false);
        traineeRepo.save(user);
        log.info("Trainee saved to Db - {} ",user.toString());
        return  signUpResponseForTrainee(user);
    }
    private SignUpResponse signUpResponseForTrainee(Trainee  user) {
        return new SignUpResponse(Constants.REGISTERED_SUCCESSFULLY, user.getUserName(), user.getName(),
                user.getRole().equals(Role.TRAINER) ? Role.TRAINER : Role.TRAINEE);
    }

    public Collection<Trainer> getTrainerByName(String name) {
         return trainerRepo.findAll(Sort.by(Sort.Direction.ASC,"name"));
    }
}
