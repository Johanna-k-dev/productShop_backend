package com.greta.productShop.daos;

import com.greta.productShop.entity.OrderProduct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public class OrderProductRowMapper implements RowMapper<OrderProduct> {

        @Override
        public OrderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderProduct(
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
            );
        }

        public void addOrderProduct(OrderProduct orderProduct) {
            String sql = "INSERT INTO order_product (order_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    orderProduct.getOrderId(),
                    orderProduct.getProductId(),
                    orderProduct.getQuantity());
        }

        public List<OrderProduct> findAllOrdersProduct() {
            String sql = "SELECT * FROM order_product";
            return jdbcTemplate.query(sql, new OrderProductRowMapper());
        }

        public OrderProduct getOrderProductById(int orderId, int productId) {
            String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_id = ?";
            return jdbcTemplate.queryForObject(sql, new OrderProductRowMapper(), orderId, productId);
        }

        public void updateOrderProduct(OrderProduct orderProduct) {
            String sql = "UPDATE order_product SET quantity = ? WHERE order_id = ? AND product_id = ?";
            jdbcTemplate.update(sql,
                    orderProduct.getQuantity(),
                    orderProduct.getOrderId(),
                    orderProduct.getProductId());
        }

        public void deleteOrderProduct(int orderId, int productId) {
            String sql = "DELETE FROM order_product WHERE order_id = ? AND product_id = ?";
            jdbcTemplate.update(sql, orderId, productId);
        }

        public boolean productExists(int productId) {
            String sql = "SELECT COUNT(*) FROM product WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
            return count != null && count > 0;
        }

    }
}
