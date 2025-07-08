package com.greta.productShop.controllers;

import com.greta.productShop.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/decrease")
    public ResponseEntity<String> decreaseStock(@RequestParam int productId, @RequestParam int quantity) {
        boolean result = stockService.decreaseStock(productId, quantity);
        return result ? ResponseEntity.ok("Stock decreased.")
                : ResponseEntity.badRequest().body("Insufficient stock.");
    }

    @PostMapping("/increase")
    public ResponseEntity<String> increaseStock(@RequestParam int productId, @RequestParam int quantity) {
        stockService.increaseStock(productId, quantity);
        return ResponseEntity.ok("Stock increased.");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isStockAvailable(@RequestParam int productId, @RequestParam int quantity) {
        boolean available = stockService.isStockAvailable(productId, quantity);
        return ResponseEntity.ok(available);
    }
}


