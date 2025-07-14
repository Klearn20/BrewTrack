package com.Brew_Track.Cafe.Brew_Track.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<String> signUp(Map<String, String> requestMap);
	ResponseEntity<String> login(Map<String, String> requestMap);

}
