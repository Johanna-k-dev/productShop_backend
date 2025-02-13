package com.greta.productShop.controllers;

import com.greta.productShop.daos.OrderDao;
import com.greta.productShop.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderDao orderDao;

    @Autowired
    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    // Ajouter une nouvelle commande
    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        try {
            orderDao.addOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body("Commande ajoutée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    // Récupérer toutes les commandes
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderDao.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Récupérer une commande par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        try {
            Order order = orderDao.getOrderById(id);
            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Mettre à jour une commande
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable int id, @RequestBody Order order) {
        try {
            order.setId(id); // Assurez-vous que l'ID est bien celui de la commande à mettre à jour
            orderDao.updateOrder(order);
            return ResponseEntity.ok("Commande mise à jour avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }
    }

    // Supprimer une commande par son ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable int id) {
        try {
            orderDao.deleteOrder(id);
            return ResponseEntity.ok("Commande supprimée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de la commande : " + e.getMessage());
        }
    }
}

