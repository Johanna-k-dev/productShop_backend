package com.greta.productShop.daos;

import com.greta.productShop.entity.OrderProduct;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static class OrderProductRowMapper implements RowMapper<OrderProduct> {
        @Override
        public OrderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderProduct(
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
            );
        }
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        String sql = "INSERT INTO order_product (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    orderProduct.getOrderId(),
                    orderProduct.getProductId(),
                    orderProduct.getQuantity());
        } catch (DataAccessException ex) {
            System.err.println("Error while adding order_product: " + ex.getMessage());
        }
    }

    public List<OrderProduct> findAllOrdersProduct() {
        String sql = "SELECT * FROM order_product";
        try {
            return jdbcTemplate.query(sql, new OrderProductRowMapper());
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching all order_product records: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    public OrderProduct getOrderProductById(int orderId, int productId) {
        String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new OrderProductRowMapper(), orderId, productId);
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching order_product by ids: " + ex.getMessage());
            return null;
        }
    }

    public void updateOrderProduct(OrderProduct orderProduct) {
        String sql = "UPDATE order_product SET quantity = ? WHERE order_id = ? AND product_id = ?";
        try {
            jdbcTemplate.update(sql,
                    orderProduct.getQuantity(),
                    orderProduct.getOrderId(),
                    orderProduct.getProductId());
        } catch (DataAccessException ex) {
            System.err.println("Error while updating order_product: " + ex.getMessage());
        }
    }

    public void deleteOrderProduct(int orderId, int productId) {
        String sql = "DELETE FROM order_product WHERE order_id = ? AND product_id = ?";
        try {
            jdbcTemplate.update(sql, orderId, productId);
        } catch (DataAccessException ex) {
            System.err.println("Error while deleting order_product: " + ex.getMessage());
        }
    }

    public boolean productExists(int productId) {
        String sql = "SELECT COUNT(*) FROM product WHERE id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            System.err.println("Error while checking if product exists: " + ex.getMessage());
            return false;
        }
    }
}
