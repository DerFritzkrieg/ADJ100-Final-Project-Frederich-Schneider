package com.finalProject.main.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finalProject.main.model.User;

@Repository
public interface IUserRepo extends JpaRepository<User, Long> {
	
	Optional<User>findByUserName(String username);
}
