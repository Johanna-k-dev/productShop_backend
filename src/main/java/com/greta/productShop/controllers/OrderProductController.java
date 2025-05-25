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

    @GetMapping("/all")
    public ResponseEntity<List<OrderProduct>> findAllOrderProduct() {
        try {
            List<OrderProduct> ordersProducts = orderProductDao.findAllOrdersProduct();
            return ResponseEntity.ok(ordersProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrderProduct(@RequestBody OrderProduct orderProduct) {
        orderProductDao.addOrderProduct(orderProduct);
        return ResponseEntity.ok("Produit ajouté à la commande avec succès");
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getOrderProductById(@PathVariable int id) {
        OrderProduct orderProduct = orderProductDao.getOrderProductById(id);
        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable int id, @RequestBody OrderProduct orderProduct) {
        try {
            orderProduct.setId(id);
            orderProductDao.updateOrderProduct(orderProduct);
            return ResponseEntity.ok("Commande mise à jour avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrderProduct(@PathVariable int id) {
        try {
            orderProductDao.deleteOrderProduct(id);
            return ResponseEntity.ok("Produit supprimé de la commande avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }
}
