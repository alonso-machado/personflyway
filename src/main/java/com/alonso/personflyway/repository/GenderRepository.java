package com.alonso.personflyway.repository;

import com.alonso.personflyway.model.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
	Optional<Gender> findByName(String genderString);
}
