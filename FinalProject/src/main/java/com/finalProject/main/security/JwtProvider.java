package com.finalProject.main.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.finalProject.main.exception.SpringBlogException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtProvider {
	//Key for signing the JWT
	private KeyStore keyStore;
	
	
	@PostConstruct
	public void init() throws SpringBlogException {
		try {
			keyStore = KeyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
			keyStore.load(resourceAsStream, "Password1!".toCharArray());
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new SpringBlogException("Exception occured while loading keystore");
		}
	}
	
	private PrivateKey getPrivateKey() throws SpringBlogException {
		// TODO Auto-generated method stub
			try {
				return (PrivateKey) keyStore.getKey("springblog", "Password1!".toCharArray());
			} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				throw new SpringBlogException("Exception occured while retrieving public key from keystore");
			}
	}
	
	private PublicKey getPublicKey() throws SpringBlogException {
		// TODO Auto-generated method stub
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			throw new SpringBlogException("Exception occured while retrieving public key from keystore");
		}
	}
	
	
	
	public String generateToken(Authentication authentication) throws InvalidKeyException, SpringBlogException {
		User principal = (User)authentication.getPrincipal();
		return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey()).compact();	//Signs the JWT
		
	}

	public boolean validateToken (String jwt) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, SpringBlogException{
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}



	public String getUsernameFromJWT(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, SpringBlogException {
		// TODO Auto-generated method stub
		Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
