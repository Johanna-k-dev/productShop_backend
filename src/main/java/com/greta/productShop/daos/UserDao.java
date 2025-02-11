package com.greta.productShop.daos;

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
    this.jdbcTemplate= jdbcTemplate;
}

    private final RowMapper<User> rowMapper = (rs, rowNum) -> new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("firstName"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getInt("postalNumber"),
            rs.getString("phoneNumber")
    );
    @Override
    public void save(User entity) {

    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void deleteById(int id) {

    }

    // Méthode pour vérifier si l'utilisateur existe déjà par son email
    public boolean ifUserExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0; // Si le count est supérieur à 0, l'utilisateur existe
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public RowMapper<User> getRowMapper() {
        return rowMapper;
    }

}
