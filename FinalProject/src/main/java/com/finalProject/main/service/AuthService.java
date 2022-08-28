package com.finalProject.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finalProject.main.dto.LoginRequest;
import com.finalProject.main.dto.RegisterRequest;
import com.finalProject.main.exception.SpringBlogException;
import com.finalProject.main.model.User;
import com.finalProject.main.repository.IUserRepo;
import com.finalProject.main.security.JwtProvider;

import io.jsonwebtoken.security.InvalidKeyException;

@Service
public class AuthService {
	
	@Autowired
	private IUserRepo userRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtProvider jwtProvider;
	
	//Create a new User and save it
	public void signup(RegisterRequest newUser) {
		User user = new User();
		user.setUserName(newUser.getUsername());
		user.setPassword(passwordEncoder(newUser.getPassword()));
		user.setEmail(newUser.getEmail());
		userRepo.save(user);
	}

	//Encrypts the password so it does not appear as plain text in the database
	private String passwordEncoder(String password) {
		// TODO Auto-generated method stub
		return encoder.encode(password);
	}
	
	public AuthenticationResponse login(LoginRequest loginReq) throws InvalidKeyException, SpringBlogException {
		Authentication authenticate = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String authenticationToken = jwtProvider.generateToken(authenticate);
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setAuthenticationToken(authenticationToken);
		authResponse.setUsername(loginReq.getUsername());
		return authResponse;
	}

	public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
		// TODO Auto-generated method stub
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Optional.of(principal);
	}
}
