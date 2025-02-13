package com.greta.productShop.controllers;

import com.greta.productShop.entity.Product;
import com.greta.productShop.daos.ProductDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Ajouter un produit
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            productDao.save(product);  // Si un doublon est inséré, une exception sera levée
            return new ResponseEntity<>("Produit ajouté avec succès.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de l'ajout du produit. Vérifiez si le produit existe déjà.", HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer tous les produits
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productDao.findAll();
        return products.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Récupérer un produit par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productDao.findById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Mettre à jour un produit
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Optional<Product> existingProduct = productDao.findById(id);
        if (existingProduct.isEmpty()) {
            return new ResponseEntity<>("Produit non trouvé.", HttpStatus.NOT_FOUND);
        }
        product.setId(id);  // Met à jour l'ID du produit
        productDao.update(product);
        return new ResponseEntity<>("Produit mis à jour avec succès.", HttpStatus.OK);
    }

    // Supprimer un produit par son ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Optional<Product> product = productDao.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>("Produit non trouvé.", HttpStatus.NOT_FOUND);
        }
        productDao.deleteById(id);
        return new ResponseEntity<>("Produit supprimé avec succès.", HttpStatus.OK);
    }
}

