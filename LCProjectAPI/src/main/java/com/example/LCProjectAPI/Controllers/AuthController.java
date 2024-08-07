package com.example.LCProjectAPI.Controllers;

import com.example.LCProjectAPI.Models.DTO.LoginFormDTO;
import com.example.LCProjectAPI.Models.DTO.RegistrationFormDTO;
import com.example.LCProjectAPI.Models.User;
import com.example.LCProjectAPI.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    private User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    private void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getUser_id());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> processRegistrationForm(
            @RequestBody RegistrationFormDTO registrationFormDTO,
            HttpServletRequest request) {
        logger.info("Processing registration for username: {}", registrationFormDTO.getUsername());

        Map<String, String> responseBody = new HashMap<>();
        try {
            if (userRepository.findByUsername(registrationFormDTO.getUsername()) == null &&
                    !registrationFormDTO.getUsername().isEmpty() &&
                    !registrationFormDTO.getPassword().isEmpty() &&
                    registrationFormDTO.getPassword().equals(registrationFormDTO.getVerifyPassword())) {

                User newUser = new User(
                        registrationFormDTO.getUsername(),
                        registrationFormDTO.getPassword()
                );
                userRepository.save(newUser);
                setUserInSession(request.getSession(), newUser);

                responseBody.put("message", "User successfully registered.");
                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            } else if (userRepository.findByUsername(registrationFormDTO.getUsername()) != null) {
                responseBody.put("message", "User already exists.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else if (registrationFormDTO.getUsername().isEmpty()) {
                responseBody.put("message", "Username required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else if (registrationFormDTO.getPassword().isEmpty()) {
                responseBody.put("message", "Password required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else if (!registrationFormDTO.getPassword().equals(registrationFormDTO.getVerifyPassword())) {
                responseBody.put("message", "Passwords do not match.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            } else {
                responseBody.put("message", "Registration failed due to invalid data.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }
        } catch (Exception ex) {
            logger.error("Error during registration: ", ex);
            responseBody.put("message", "An error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> processLoginForm(
            @RequestBody LoginFormDTO loginFormDTO,
            HttpServletRequest request) {
        logger.info("Processing login for username: {}", loginFormDTO.getUsername());

        Map<String, String> responseBody = new HashMap<>();
        User user = userRepository.findByUsername(loginFormDTO.getUsername());
        if (user == null) {
            responseBody.put("message", "Username does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else if (!user.isMatch(loginFormDTO.getPassword())) {
            responseBody.put("message", "Password does not match.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else {
            setUserInSession(request.getSession(), user);
            responseBody.put("message", "User successfully logged in.");
            responseBody.put("username", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        User user = getUserFromSession(request.getSession(false));
        if (user != null) {
            responseBody.put("username", user.getUsername());
            // Add other user details if needed
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody User updatedUser, HttpServletRequest request) {
        Map<String, String> responseBody = new HashMap<>();
        User sessionUser = getUserFromSession(request.getSession(false));
        if (sessionUser != null) {
            sessionUser.setUsername(updatedUser.getUsername());
            sessionUser.setPassword(updatedUser.getPassword()); // Consider hashing the password
            userRepository.save(sessionUser);
            responseBody.put("message", "User updated successfully");
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }
}
