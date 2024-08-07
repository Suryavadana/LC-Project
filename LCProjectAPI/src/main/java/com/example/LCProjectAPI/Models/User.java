package com.example.LCProjectAPI.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Constructors, getters, setters, etc.

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password); // Hash the password
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password); // Hash the password
    }

    public boolean isMatch(String password) {
        return new BCryptPasswordEncoder().matches(password, this.password);
    }
}
