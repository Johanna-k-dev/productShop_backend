package com.greta.productShop.controllers;

import com.greta.productShop.daos.OrderDao;
import com.greta.productShop.daos.UserDao;
import com.greta.productShop.dto.OrderWithProductsDto;
import com.greta.productShop.entity.Order;
import com.greta.productShop.entity.OrderProduct;
import com.greta.productShop.daos.OrderProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order-products")
public class OrderProductController {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private  final UserDao userDao;

    @Autowired
    public OrderProductController(OrderDao orderDao, OrderProductDao orderProductDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.userDao= userDao;
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

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderProduct> getOrderProductById(
            @PathVariable int orderId,
            @PathVariable int productId) {
        OrderProduct orderProduct = orderProductDao.getOrderProductById((int) orderId, (int) productId);
        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderProduct);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrderWithProducts(@RequestBody OrderWithProductsDto orderDto, Authentication authentication) {
        try {

            for (OrderProduct item : orderDto.getItems()) {
                if (!orderProductDao.productExists((int) item.getProductId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Erreur : Le produit avec l’ID " + item.getProductId() + " n’existe pas.");
                }
            }

            // 2. Création de la commande
            Order order = new Order();
           int userId= userDao.getUserIdFromAuth(authentication);
            order.setUserId(userId);
            order.setDate(LocalDate.now());
            order.setTotal(orderDto.getTotal());

            int orderId = Math.toIntExact(orderDao.addOrder(order)); // Méthode personnalisée qui retourne l’ID généré

            // 3. Insertion des produits liés à cette commande
            for (OrderProduct item : orderDto.getItems()) {
                item.setOrderId(orderId); // on attache la commande générée
                orderProductDao.addOrderProduct(item); // insertion dans order_product
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Commande et produits enregistrés !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l’enregistrement : " + e.getMessage());
        }
    }




    @PutMapping("/update/{orderId}/{productId}")
    public ResponseEntity<String> updateOrderProduct(
            @PathVariable int orderId,
            @PathVariable int productId,
            @RequestBody OrderProduct orderProduct) {
        try {
            orderProduct.setOrderId(orderId);
            orderProduct.setProductId(productId);
            orderProductDao.updateOrderProduct(orderProduct);
            return ResponseEntity.ok("Commande mise à jour avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderId}/{productId}")
    public ResponseEntity<String> deleteOrderProduct(
            @PathVariable int orderId,
            @PathVariable int productId) {
        try {
            orderProductDao.deleteOrderProduct((int) orderId, (int) productId);
            return ResponseEntity.ok("Produit supprimé de la commande avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }
}
