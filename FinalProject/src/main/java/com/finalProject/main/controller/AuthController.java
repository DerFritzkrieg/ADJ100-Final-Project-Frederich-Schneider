package com.finalProject.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalProject.main.dto.LoginRequest;
import com.finalProject.main.dto.RegisterRequest;
import com.finalProject.main.exception.SpringBlogException;
import com.finalProject.main.service.AuthService;
import com.finalProject.main.service.AuthenticationResponse;

import io.jsonwebtoken.security.InvalidKeyException;

//@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws InvalidKeyException, SpringBlogException {
		return authService.login(loginRequest);
	}
}
