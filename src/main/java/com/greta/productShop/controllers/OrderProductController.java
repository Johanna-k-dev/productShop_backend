package com.greta.productShop.controllers;


import com.greta.productShop.entity.OrderProduct;
import com.greta.productShop.daos.OrderProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-products")
public class OrderProductController {

    private final OrderProductDao orderProductDao;

    @Autowired
    public OrderProductController(OrderProductDao orderProductDao) {
        this.orderProductDao = orderProductDao;
    }

    // Récupérer toutes les commandes
    @GetMapping("/all")
    public ResponseEntity<List<OrderProduct>> findAllOrderProduct() {
        try {
            List<OrderProduct> ordersProducts = orderProductDao.findAllOrdersProduct();
            return ResponseEntity.ok(ordersProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Ajouter un produit à une commande
    @PostMapping("/add")
    public ResponseEntity<String> addOrderProduct(@RequestBody OrderProduct orderProduct) {
        orderProductDao.addOrderProduct(orderProduct);
        return ResponseEntity.ok("Produit ajouté à la commande avec succès");
    }

    // Récupérer un produit dans une commande (par ID)
    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getOrderProductById(@PathVariable int id) {
        OrderProduct orderProduct = orderProductDao.getOrderProductById(id);
        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderProduct);
    }

    // Mettre à jour une commande
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable int id, @RequestBody OrderProduct orderProduct) {
        try {
            orderProduct.setId(id); // Assurez-vous que l'ID est bien celui de la commande à mettre à jour
            orderProductDao.updateOrderProduct(orderProduct);
            return ResponseEntity.ok("Commande mise à jour avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }
    }

    // Supprimer une commande par son ID
    @DeleteMapping("/delete/{id}")
    public void deleteOrderProduct(int id) {
        String sql = "DELETE FROM `order_product` WHERE id = ?";
    }


}



