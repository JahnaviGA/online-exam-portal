package com.training.portal.repository;

import com.training.portal.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface QuestionRepo extends JpaRepository<Question, Long>{

}
