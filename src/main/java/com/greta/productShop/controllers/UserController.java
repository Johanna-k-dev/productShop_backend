package com.greta.productShop.controllers;

import com.greta.productShop.dto.UserDto;
import com.greta.productShop.entity.User;
import com.greta.productShop.daos.UserDao;
import com.greta.productShop.services.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder; // Ajout
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder; // Injection
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        System.out.println(" Tentative d'ajout de l'utilisateur : " + user);
        boolean exists = userDao.existsByEmail(user.getEmail());
        System.out.println("🔍 Utilisateur existe déjà ? " + exists);

        if (exists) {
            return ResponseEntity.badRequest().body("L'utilisateur avec cet email existe déjà.");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userDao.save(user);
        System.out.println("Utilisateur ajouté avec succès !");
        return ResponseEntity.ok("Utilisateur ajouté avec succès !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        User user = userDao.findByEmail(loginData.getEmail());

        if (user == null || !passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe invalide");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "name", user.getFirstName(),
                "email", user.getEmail()
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Optionnel : invalidation côté serveur si tu stockes le token
        return ResponseEntity.ok("Déconnecté");
    }

    // User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    // User by email
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
    // Update user
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> existingUserOpt = userDao.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = existingUserOpt.get();
        // Mets à jour les informations de l'utilisateur
        existingUser.setName(user.getName());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setPostalNumber(user.getPostalNumber());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        userDao.update(existingUser);
        return ResponseEntity.ok("Utilisateur mis à jour !");
    }

    // Delete user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            userDao.deleteById(id);
            return ResponseEntity.ok("Utilisateur supprimé avec succès !");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
