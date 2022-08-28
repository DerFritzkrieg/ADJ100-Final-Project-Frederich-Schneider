package com.finalProject.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finalProject.main.model.Post;

public interface IPostRepo extends JpaRepository<Post, Long> {
	
}
