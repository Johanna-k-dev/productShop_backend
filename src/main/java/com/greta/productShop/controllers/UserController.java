package com.greta.productShop.controllers;

import com.greta.productShop.entity.User;
import com.greta.productShop.daos.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder; // Ajout

    @Autowired
    public UserController(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder; // Injection
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
        System.out.println("✅ Utilisateur ajouté avec succès !");
        return ResponseEntity.ok("Utilisateur ajouté avec succès !");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = Optional.ofNullable(userDao.findByEmail(loginRequest.getEmail()));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé.");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
        }

        return ResponseEntity.ok("Connexion réussie !");
    }
    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Optionnel : invalidation côté serveur si tu stockes le token
        return ResponseEntity.ok("Déconnecté");
    }

    // Utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    // Utilisateur par email
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

    // Met à jour un utilisateur
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

    // Supprimer un utilisateur par ID
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
