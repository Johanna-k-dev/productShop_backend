package com.greta.productShop.daos;

import com.greta.productShop.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        System.out.println("Ajout de commande pour userId = " + order.getUserId());
        return keyHolder.getKey().intValue(); // retourne l’ID généré
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM `order`";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setDate(rs.getObject("date", LocalDate.class));
            order.setTotal(rs.getDouble("total"));
            return order;
        });
    }

    public Order getOrderById(int id) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setDate(rs.getObject("date", LocalDate.class));
            order.setTotal(rs.getDouble("total"));
            return order;
        });
    }

    public Optional<Order> findOrderById(int orderId) {
        String sql = "SELECT * FROM `order` WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setDate(rs.getObject("date", LocalDate.class));
            order.setTotal(rs.getDouble("total"));
            return order;
        }, orderId).stream().findFirst();
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



