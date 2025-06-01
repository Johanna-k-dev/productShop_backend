package com.greta.productShop.controllers;

import com.greta.productShop.daos.UserDao;
import com.greta.productShop.entity.User;
import com.greta.productShop.services.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;

    public AuthController(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtil jwtUtils) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        boolean alreadyExists = userDao.existsByEmail(user.getEmail());
        if (alreadyExists) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User newUser = new User(
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                "USER"
        );

        boolean isUserSaved = userDao.save(newUser);
        return isUserSaved ?
                ResponseEntity.ok("User registered successfully!") :
                ResponseEntity.badRequest().body("Error: User registration failed!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User user) {

        User existingUser = userDao.findByEmail(user.getEmail());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Invalid email or password!");
        }

        String token = jwtUtils.generateToken(existingUser.getEmail());
        return ResponseEntity.ok(token);
    }
}

