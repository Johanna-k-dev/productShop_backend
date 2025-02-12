package com.greta.productShop.controllers;

import com.greta.productShop.entity.User;
import com.greta.productShop.daos.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    // Ajouter un utilisateur
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (userDao.ifUserExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("L'utilisateur avec cet email existe déjà.");
        }
        // Sauvegarder
        userDao.save(user);
        return ResponseEntity.ok("Utilisateur ajouté avec succès !");
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
        System.out.println("Email reçu : " + email);  // 🔥 Log pour debug

        String sql = "SELECT * FROM user WHERE email = ?";
        List<User> users = userDao.getJdbcTemplate().query(sql, userDao.getRowMapper(), email);

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users.get(0));
        }
    }

    // Met à jour un utilisateur
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
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
