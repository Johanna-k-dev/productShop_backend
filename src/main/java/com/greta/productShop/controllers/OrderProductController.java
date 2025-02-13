package com.greta.productShop.controllers;

import com.greta.productShop.entity.OrderProduct;
import com.greta.productShop.daos.OrderProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-products")
public class OrderProductController {

    private final OrderProductDao orderProductDao;

    @Autowired
    public OrderProductController(OrderProductDao orderProductDao) {
        this.orderProductDao = orderProductDao;
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
}