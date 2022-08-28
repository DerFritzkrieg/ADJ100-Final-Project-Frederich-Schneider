package com.finalProject.main.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.finalProject.main.dto.PostDto;
import com.finalProject.main.exception.PostNotFoundException;
import com.finalProject.main.model.Post;
import com.finalProject.main.repository.IPostRepo;

@Service
public class PostService {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private IPostRepo postRepo;
	
	public List<PostDto> showAllPosts(){
		List<Post> posts = postRepo.findAll();
		return posts.stream().map(this::mapFromPostToDto).toList();
	}
	
	public PostDto readSinglePost(Long id) {
		Post post = postRepo.findById(id).orElseThrow(()-> new PostNotFoundException("For id " + id));
		return mapFromPostToDto(post);
	}
	
	/*public void createPost(PostDto postDto) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		User username = authService.getCurrentUser().orElseThrow(()->new IllegalArgumentException("No User logged in"));
		post.setUsername(username.getUsername());
		post.setCreatedOn(Instant.now());
		postRepo.save(post);
		
	}*/
	
	public void createPost(PostDto postDto) {
		Post post = mapFromDtoToPost(postDto);
		postRepo.save(post);
	}
	
	private PostDto mapFromPostToDto(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setContent(post.getContent());
		postDto.setUsername(post.getUsername());
		return postDto;
	}
	
	private Post mapFromDtoToPost(PostDto postDto) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		User loggedInUser = authService.getCurrentUser().orElseThrow(()->new IllegalArgumentException("User Not Found"));
		post.setCreatedOn(Instant.now());
		post.setUsername(loggedInUser.getUsername());
		post.setUpdatedOn(Instant.now());
		return post;
	}
}
