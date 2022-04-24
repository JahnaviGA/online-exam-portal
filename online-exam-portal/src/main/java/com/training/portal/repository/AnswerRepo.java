package com.training.portal.repository;

import com.training.portal.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AnswerRepo extends JpaRepository<Answer, Long>{
}
