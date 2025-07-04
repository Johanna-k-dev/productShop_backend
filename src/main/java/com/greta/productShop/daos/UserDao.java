package com.greta.productShop.daos;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.greta.productShop.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements CrudDao<User> {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("first_name"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getString("postal_number"),
            rs.getString("phone_number"),
            rs.getString("password"),
            rs.getString("role")
    );

    @Override
    public boolean save(User entity) {
        String sql = "INSERT INTO user (email, name, first_name, address, phone_number, postal_number, password, role ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = jdbcTemplate.update(sql,
                    entity.getEmail(),
                    entity.getName(),
                    entity.getFirstName(),
                    entity.getAddress(),
                    entity.getPhoneNumber(),
                    entity.getPostalNumber(),
                    entity.getPassword(),
                    entity.getRole()
            );
            return rows > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while saving user: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            List<User> users = jdbcTemplate.query(sql, rowMapper, id);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching user by id: " + ex.getMessage());
            return Optional.empty();
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            return jdbcTemplate.query(sql, rowMapper, email)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching user by email: " + ex.getMessage());
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }
    }

    public int getUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Une erreur est survenue lors de l'authentification");
        }
        try {
            String email = authentication.getName();
            User user = findByEmail(email);
            return user.getId();
        } catch (UsernameNotFoundException ex) {
            System.err.println("Error while retrieving user id from authentication: " + ex.getMessage());
            throw new RuntimeException("Utilisateur non trouvé dans le contexte d'authentification");
        }
    }

    @Override
    public void update(User entity) {
        String sql = "UPDATE user SET email = ?, name = ?, first_name = ?, address = ?, phone_number = ?, postal_number = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql,
                    entity.getEmail(),
                    entity.getName(),
                    entity.getFirstName(),
                    entity.getAddress(),
                    entity.getPhoneNumber(),
                    entity.getPostalNumber(),
                    entity.getId());
        } catch (DataAccessException ex) {
            System.err.println("Error while updating user: " + ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException ex) {
            System.err.println("Error while deleting user: " + ex.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching all users: " + ex.getMessage());
            return List.of();
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while checking if user exists by email: " + ex.getMessage());
            return false;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public RowMapper<User> getRowMapper() {
        return rowMapper;
    }
}

