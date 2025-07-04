package com.greta.productShop.daos;

import com.greta.productShop.entity.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setDate(rs.getObject("date", LocalDate.class));
            order.setTotal(rs.getDouble("total"));
            order.setUserId(rs.getInt("user_id"));
            return order;
        }
    }

    public int addOrder(Order order) {
        String sql = "INSERT INTO `order` (date, total, user_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setDate(1, Date.valueOf(order.getDate()));
                ps.setDouble(2, order.getTotal());
                ps.setInt(3, order.getUserId());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            return key != null ? key.intValue() : -1;
        } catch (DataAccessException ex) {
            System.err.println("Error while inserting order: " + ex.getMessage());
            return -1;
        }
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM `order`";
        try {
            return jdbcTemplate.query(sql, new OrderRowMapper());
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching all orders: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

    public Order getOrderById(int id) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching order by id: " + ex.getMessage());
            return null;
        }
    }

    public Optional<Order> findOrderById(int orderId) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, new OrderRowMapper(), orderId)
                    .stream().findFirst();
        } catch (DataAccessException ex) {
            System.err.println("Error while fetching order by id: " + ex.getMessage());
            return Optional.empty();
        }
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE `order` SET date = ?, total = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, order.getDate(), order.getTotal(), order.getId());
        } catch (DataAccessException ex) {
            System.err.println("Error while updating order: " + ex.getMessage());
        }
    }

    public void deleteOrder(int id) {
        String sql = "DELETE FROM `order` WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException ex) {
            System.err.println("Error while deleting order: " + ex.getMessage());
        }
    }
}




