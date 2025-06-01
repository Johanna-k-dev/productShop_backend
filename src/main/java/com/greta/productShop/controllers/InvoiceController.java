package com.greta.productShop.controllers;

import com.greta.productShop.entity.Invoice;
import com.greta.productShop.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateInvoice(@RequestParam int orderId) {
        boolean success = invoiceService.generateInvoice(orderId);
        return success ? ResponseEntity.ok("Invoice generated successfully")
                : ResponseEntity.badRequest().body("Order not found or invoice already exists");
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable int orderId) {
        Optional<Invoice> invoice = invoiceService.getInvoiceByOrderId(orderId);
        return invoice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
