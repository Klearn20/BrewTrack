package com.Brew_Track.Cafe.Brew_Track.serviceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.Brew_Track.Cafe.Brew_Track.JWT.CustomerUsersDetailsService;
import com.Brew_Track.Cafe.Brew_Track.JWT.JwtUtil;
import com.Brew_Track.Cafe.Brew_Track.POJO.User;
import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
import com.Brew_Track.Cafe.Brew_Track.dao.UserDao;
import com.Brew_Track.Cafe.Brew_Track.service.UserService;
import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Exception in signUp: {}", ex.getMessage());
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") &&
               requestMap.containsKey("contactNumber") &&
               requestMap.containsKey("email") &&
               requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false"); // Pending approval
        user.setRole("user");    // Default role
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestMap.get("email"), requestMap.get("password")
                    )
            );

            if (auth.isAuthenticated()) {
                User userDetail = customerUsersDetailsService.getUserDetail();
                if ("true".equalsIgnoreCase(userDetail.getStatus())) {
                    String token = jwtUtil.generateToken(userDetail.getEmail(), userDetail.getRole());
                    return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "{\"message\":\"Wait for admin approval\"}",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }

        } catch (AuthenticationException ex) {
            log.error("Error in login: {}", ex.getMessage());
        }

        return new ResponseEntity<>(
                "{\"message\":\"Bad Credentials.\"}",
                HttpStatus.BAD_REQUEST
        );
    }
}
