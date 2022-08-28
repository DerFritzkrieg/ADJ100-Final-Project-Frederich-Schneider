package com.finalProject.main.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finalProject.main.model.User;
import com.finalProject.main.repository.IUserRepo;

@Service
public class UserDetailsServiceImplementer implements UserDetailsService{
	
	@Autowired
	private IUserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		//Checks if user exists and sets it to a user object
		User user = userRepo.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("No user found " + username));
		
		//Returns user object with the role of User
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), true, true, true, true, getAuthorities("ROLE_USER"));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role_user){
		return Collections.singletonList(new SimpleGrantedAuthority(role_user));
	}
	
	
}
