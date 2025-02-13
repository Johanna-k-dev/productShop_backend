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
    // Récupérer tous les OrderProduct
    public List<OrderProduct> findAllOrdersProduct() {
        String sql = "SELECT * FROM order_product";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderProduct(
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("user_id"),
                        rs.getInt("quantity")
                )
        );
    }
    // Ajouter un produit à une commande
    public void addOrderProduct(OrderProduct orderProduct) {
        try {
            String sql = "INSERT INTO order_product (order_id, user_id ,product_id, quantity) VALUES (?, ?, ?, ? )";
            jdbcTemplate.update(sql, orderProduct.getOrderId(),orderProduct.getUserId(), orderProduct.getProductId(), orderProduct.getQuantity());
        } catch (DataAccessException e) {
            throw new RuntimeException("Erreur lors de l'ajout du produit à la commande", e);
        }
    }

    // Récupérer un produit dans une commande (par ID)
    public OrderProduct getOrderProductById(int id) {
        String sql = "SELECT * FROM order_product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrderProduct(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                )
        );
    }

    // Mise à jour d'un OrderProduct
    public void updateOrderProduct(OrderProduct orderProduct) {
        String sql = "UPDATE order_product SET order_id = ?,SET user_id ?,SET product_id = ?,SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getQuantity(), orderProduct.getId());
    }


    // Supprimer un produit d'une commande par ID
    public void deleteOrderProduct(int id) {
        try {
            String sql = "DELETE FROM order_product WHERE id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erreur lors de la suppression du produit de la commande", e);
        }
    }
}



