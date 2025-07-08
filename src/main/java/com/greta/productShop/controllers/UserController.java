package com.greta.productShop.controllers;

import com.greta.productShop.dto.UserDto;
import com.greta.productShop.entity.User;
import com.greta.productShop.daos.UserDao;
import com.greta.productShop.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder; // Injection
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            boolean exists = userDao.existsByEmail(user.getEmail());
            if (exists) {
                return ResponseEntity.badRequest().body("L'utilisateur avec cet email existe déjà.");
            }

            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            userDao.save(user);

            return ResponseEntity.ok("Utilisateur ajouté avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        System.out.println("Email reçu : " + email);  // Log pour debug

        String sql = "SELECT * FROM user WHERE email = ?";
        List<User> users = userDao.getJdbcTemplate().query(sql, userDao.getRowMapper(), email);

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users.get(0));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails authenticatedUser) {
        String email = authenticatedUser.getUsername();
        User user = userDao.findByEmail(email);
        UserDto dto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getFirstName(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getPostalNumber(),
                user.getRole()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            Optional<User> existingUserOpt = userDao.findById(id);
            if (existingUserOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User existingUser = existingUserOpt.get();
            existingUser.setName(user.getName());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            existingUser.setPostalNumber(user.getPostalNumber());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            userDao.update(existingUser);
            return ResponseEntity.ok("Utilisateur mis à jour !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            Optional<User> user = userDao.findById(id);
            if (user.isPresent()) {
                userDao.deleteById(id);
                return ResponseEntity.ok("Utilisateur supprimé avec succès !");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
