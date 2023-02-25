package com.project.ridealong.dao;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ridealong.entities.Riders;


public interface RidersDao extends JpaRepository<Riders,Long> {
	  List<Riders> findByEmailContaining(String email);
	  Optional<Riders> findByEmail(String email);
}
