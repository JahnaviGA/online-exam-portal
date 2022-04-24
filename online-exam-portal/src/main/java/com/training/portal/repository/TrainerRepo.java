package com.training.portal.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.training.portal.model.Trainer;

@Repository
public interface TrainerRepo extends JpaRepository<Trainer,Integer> {

	//@Query(value = "from Trainer where name = ?0")
	Trainer findByName(String name);

	//@Query("SELECT t FROM Trainer t where t.email = ?0")
	Optional<Trainer> findByEmail(String email);
}


