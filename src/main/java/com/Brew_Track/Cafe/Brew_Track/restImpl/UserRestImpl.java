package com.Brew_Track.Cafe.Brew_Track.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
import com.Brew_Track.Cafe.Brew_Track.rest.UserRest;
import com.Brew_Track.Cafe.Brew_Track.service.UserService;
import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;



@RestController
public class UserRestImpl implements UserRest {
    
	@Autowired
	UserService userService;
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userService.signUp(requestMap);
		}catch (Exception ex) {
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG ,HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {	
		// TODO Auto-generated method stub
		try {
			return userService.login(requestMap);
		} catch (Exception ex) {
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
