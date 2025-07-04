package com.greta.productShop.daos;

import com.greta.productShop.entity.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
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

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(order.getDate()));
            ps.setDouble(2, order.getTotal());
            ps.setInt(3,order.getUserId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue(); // retourne l’ID généré
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM `order`";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    public Order getOrderById(int id) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
    }

    public Optional<Order> findOrderById(int orderId) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), orderId)
                .stream().findFirst();
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE `order` SET date = ?, total = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getDate(), order.getTotal(), order.getId());
    }

    public void deleteOrder(int id) {
        String sql = "DELETE FROM `order` WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}



