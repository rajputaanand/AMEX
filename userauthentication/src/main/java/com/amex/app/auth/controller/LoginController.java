package com.amex.app.auth.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amex.app.auth.repository.UserRepository;
import com.amex.app.auth.user.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
	 private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
		String username = credentials.get("username");
		String password = credentials.get("password");
		log.info(credentials.toString());
		// Check if user exists
		Optional<User> optionalUser = userRepository.findByUsername(username);
		log.info(optionalUser.toString());
		if (!optionalUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}

		User user = optionalUser.get();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
		}

		// Generate JWT token
		//String token = generateJWTToken(user);

		// Return token
		return ResponseEntity.ok("User Logged In");
	}

	private String generateJWTToken(User user) {
		// Set expiration time for the token (e.g., 1 hour)
		long expirationTime = System.currentTimeMillis() + 3600000; // 1 hour
		Date expirationDate = new Date(expirationTime);

		// Generate JWT token
		String token = Jwts.builder().setSubject(user.getUsername()).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, "yourSecretKey") // Replace "yourSecretKey" with your actual secret
																		// key
				.compact();

		return token;
	}
}
