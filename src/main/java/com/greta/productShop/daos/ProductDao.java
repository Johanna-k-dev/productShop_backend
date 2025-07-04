package com.greta.productShop.daos;

import com.greta.productShop.entity.Product;
import org.springframework.dao.DataAccessException;
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
            rs.getInt("collection_id"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("description"),
            rs.getString("poster_path")
    );

    @Override
    public boolean save(Product product) {
        String sql = "INSERT INTO product (name, collection_id, price, quantity, description, poster_path) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            int rows = jdbcTemplate.update(sql,
                    product.getName(),
                    product.getCollection(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getDescription(),
                    product.getPosterPath());
            return rows > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while inserting product: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Product> findById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching product by id: " + ex.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Product> findByName(String name) {
        String sql = "SELECT * FROM product WHERE name = ?";
        try {
            return jdbcTemplate.query(sql, rowMapper, name).stream().findFirst();
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching product by name: " + ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching all products: " + ex.getMessage());
            return List.of();
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name=?,collection_id=?, price=?, quantity=?, description=?, poster_path=? WHERE id=?";
        try {
            jdbcTemplate.update(sql,
                    product.getName(),
                    product.getCollection(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getDescription(),
                    product.getPosterPath(),
                    product.getId());
        } catch (DataAccessException ex) {
            System.err.println("Error while updating product: " + ex.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException ex) {
            System.err.println("Error while deleting product: " + ex.getMessage());
        }
    }

    public List<Product> findByCollection(int collectionId) {
        String sql = "SELECT * FROM product WHERE collection_id = ?";
        try {
            return jdbcTemplate.query(sql, rowMapper, collectionId);
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching products by collection: " + ex.getMessage());
            return List.of();
        }
    }

    public boolean ifProductExists(String productName) {
        String sql = "SELECT COUNT(*) FROM product WHERE name = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productName);
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while checking if product exists: " + ex.getMessage());
            return false;
        }
    }
}

