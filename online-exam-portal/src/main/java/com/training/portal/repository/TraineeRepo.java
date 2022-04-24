package com.training.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.training.portal.model.Trainee;
@Repository
public interface TraineeRepo extends JpaRepository<Trainee,String> {

    Trainee findByName(String traineeName);
}
