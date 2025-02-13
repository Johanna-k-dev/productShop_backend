package com.greta.productShop.daos;

import com.greta.productShop.entity.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Ajouter un produit à une commande
    public void addOrderProduct(OrderProduct orderProduct) {
        try {
            String sql = "INSERT INTO order_product (order_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getQuantity());
        } catch (DataAccessException e) {
            // Gestion des erreurs
            throw new RuntimeException("Erreur lors de l'ajout du produit à la commande", e);
        }
    }

    // Récupérer un produit dans une commande (par ID)
    public OrderProduct getOrderProductById(int id) {
        String sql = "SELECT * FROM order_product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(rs.getInt("id"));
            orderProduct.setOrderId(rs.getInt("order_id"));
            orderProduct.setProductId(rs.getInt("product_id"));
            orderProduct.setQuantity(rs.getInt("quantity"));
            return orderProduct;
        });
    }
}
