package com.greta.productShop.daos;



import com.greta.productShop.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    // Injection de JdbcTemplate via le constructeur
    @Autowired
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Ajouter une commande
    public void addOrder(Order order) {
        String sql = "INSERT INTO `order` (date, total) VALUES (?, ?)";
        jdbcTemplate.update(sql, order.getDate(), order.getTotal());
    }

    // Récupérer toutes les commandes
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM `order`";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setDate(rs.getObject("date", LocalDate.class)); // Date au format LocalDate
            order.setTotal(rs.getDouble("total"));
            return order;
        });
    }

    // Récupérer une commande par son ID
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

    // Mettre à jour une commande
    public void updateOrder(Order order) {
        String sql = "UPDATE `order` SET date = ?, total = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getDate(), order.getTotal(), order.getId());
    }

    // Supprimer une commande par son ID
    public void deleteOrder(int id) {
        String sql = "DELETE FROM `order` WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}


