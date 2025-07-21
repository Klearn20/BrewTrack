    package com.Brew_Track.Cafe.Brew_Track.serviceImpl;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.Objects;
    import java.util.Optional;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.stereotype.Service;

    import com.Brew_Track.Cafe.Brew_Track.JWT.CustomerUsersDetailsService;
    import com.Brew_Track.Cafe.Brew_Track.JWT.JwtFilter;
    import com.Brew_Track.Cafe.Brew_Track.JWT.JwtUtil;
    import com.Brew_Track.Cafe.Brew_Track.POJO.User;
    import com.Brew_Track.Cafe.Brew_Track.constents.CafeConstants;
    import com.Brew_Track.Cafe.Brew_Track.dao.UserDao;
    import com.Brew_Track.Cafe.Brew_Track.service.UserService;
    import com.Brew_Track.Cafe.Brew_Track.utils.CafeUtils;
    import com.Brew_Track.Cafe.Brew_Track.utils.EmailUtils;
    import com.Brew_Track.Cafe.Brew_Track.wrapper.UserWrapper;
import com.google.common.base.Strings;

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

        @Autowired
        JwtFilter jwtFilter;

        @Autowired
        EmailUtils emailUtils;

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
                        requestMap.get("email"),
                        requestMap.get("password")
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

        @Override
        public ResponseEntity<List<UserWrapper>> getAllUser() {
            try {
                if (jwtFilter.isAdmin()) {
                    List<UserWrapper> userList = userDao.getAllUser();
                    return new ResponseEntity<>(userList, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception ex) {
                log.error("Exception in getAllUser: {}", ex.getMessage());
            }
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Override
        public ResponseEntity<String> update(Map<String, String> requestMap) {
            try {
                if (jwtFilter.isAdmin()) {

                    log.info("Admin performing update: {}", jwtFilter.getCurrentUser());
                    System.out.println("JWT claims: " + jwtFilter.getClaims());
                    System.out.println("Extracted username: " + jwtFilter.getCurrentUser());
                    Optional<User> optional = userDao.findById(Integer.valueOf(requestMap.get("id"))); 
                    if (!optional.isEmpty()) {
                        userDao.updateUser(requestMap.get("status"), Integer.valueOf(requestMap.get("id")));
                        sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                        return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return CafeUtils.getResponseEntity("Unauthorized access", HttpStatus.UNAUTHORIZED);
                }
            } catch (NumberFormatException ex) {
                log.error("Invalid ID format: {}", ex.getMessage());
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } catch (Exception ex) {
                log.error("Unexpected exception in update: {}", ex.getMessage());
                return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        private void sendMailToAllAdmin (String status, String email, List<String> allAdmin) {
            allAdmin.remove(jwtFilter.getCurrentUser());
            if (status != null && status.equalsIgnoreCase("true")) {
                emailUtils.sendSimpleMessage(
                jwtFilter.getCurrentUser(),
                "Account Approved",
                "USER: " + email + "\nhas been approved by\nADMIN: " + jwtFilter.getCurrentUser(),
                allAdmin);
            } else {
                emailUtils.sendSimpleMessage(
                jwtFilter.getCurrentUser(),
                "Account Disabled",
                "USER: " + email + "\nhas been disabled by\nADMIN: " + jwtFilter.getCurrentUser(),
                allAdmin);
            }
        }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
        @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());

            if(userObj != null) {
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtils.getResponseEntity("Password changed successfully", HttpStatus.OK);
                } 
                
                return CafeUtils.getResponseEntity("Invalid old password", HttpStatus.BAD_REQUEST);
            } 
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            
        } catch (Exception e) {
             e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
        
    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) 
                emailUtils.forgotMail(user.getEmail(), "Brew Track - Forgot Password", user.getPassword());
            return CafeUtils.getResponseEntity("Email sent successfully", HttpStatus.OK); 
              
        } catch (Exception ex) {
            log.error("Error in forgotPassword: {}", ex.getMessage(), ex);
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    }
    
    