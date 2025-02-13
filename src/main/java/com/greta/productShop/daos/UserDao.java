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
            rs.getString("first_name"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getInt("postal_number"),
            rs.getString("phone_number")
    );

    @Override
    public void save(User entity) {
        // Insertion sans l'ID, car il est auto-incrémenté
        String sql = "INSERT INTO user (email, name, first_name, address, phone_number, postal_number) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                entity.getEmail(),
                entity.getName(),
                entity.getFirstName(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getPostalNumber()
        );
    }


    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, rowMapper, id);

        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public void update(User entity) {
        String sql = "UPDATE user SET email = ?, name = ?, first_name = ?, address = ?, phone_number = ?, postal_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getEmail(), entity.getName(), entity.getFirstName(), entity.getAddress(), entity.getPhoneNumber(), entity.getPostalNumber(), entity.getId());
    }



    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, rowMapper);
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
