package com.greta.productShop.daos;

import com.greta.productShop.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao implements CrudDao<Product> {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("collection"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("description"),
            rs.getString("poster_path") // Mapping du posterPath
    );

    @Override
    public boolean save(Product product) {
        String sql = "INSERT INTO product (name, collection, price, quantity, description, poster_path) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getCollection(), product.getPrice(), product.getQuantity(), product.getDescription(), product.getPosterPath());

        return false;
    }

    @Override
    public Optional<Product> findById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Optional<Product> findByName(String name) {
        String sql = "SELECT * FROM product WHERE name = ?";
        return jdbcTemplate.query(sql, rowMapper, name).stream().findFirst();
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name=?,collection=?, price=?, quantity=?, description=?, poster_path=? WHERE id=?";
        jdbcTemplate.update(sql, product.getName(), product.getCollection(), product.getPrice(), product.getQuantity(), product.getDescription(), product.getPosterPath(), product.getId());;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    public List<Product> findByCollection(int collectionId) {
        String sql = "SELECT * FROM product WHERE collection = ?";
        return jdbcTemplate.query(sql, rowMapper, collectionId);
    }
    // existing product Verified
    public boolean ifProductExists(String productName) {
        String sql = "SELECT COUNT(*) FROM product WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productName);
        return count != null && count > 0; // Si le count est supérieur à 0, le produit existe
    }
}
