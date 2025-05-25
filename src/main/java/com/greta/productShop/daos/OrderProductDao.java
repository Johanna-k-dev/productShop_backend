package com.greta.productShop.daos;

import com.greta.productShop.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderProduct> findAllOrdersProduct() {
        String sql = "SELECT * FROM order_product";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderProduct(
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                )
        );
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        String sql = "INSERT INTO order_product (order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getQuantity());
    }

    public OrderProduct getOrderProductById(int id) {
        String sql = "SELECT * FROM order_product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrderProduct(
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                )
        );
    }

    public void updateOrderProduct(OrderProduct orderProduct) {
        String sql = "UPDATE order_product SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getQuantity(), orderProduct.getId());
    }

    public void deleteOrderProduct(int id) {
        String sql = "DELETE FROM order_product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
