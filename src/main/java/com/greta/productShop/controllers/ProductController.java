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

    // Add product
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            productDao.save(product);
            return new ResponseEntity<>("Produit ajouté avec succès.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de l'ajout du produit. Vérifiez si le produit existe déjà.", HttpStatus.BAD_REQUEST);
        }
    }

    // get all product
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productDao.findAll();
        return products.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(products, HttpStatus.OK);
    }

    // get product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productDao.findById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // get product by collectionID
    @GetMapping("/collection/{collectionId}")
    public ResponseEntity<List<Product>> getProductsByCollection(@PathVariable int collectionId) {
        List<Product> products = productDao.findByCollection(collectionId);
        return products.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Optional<Product> product = productDao.findByName(name);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // update product
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            Optional<Product> existingProduct = productDao.findById(id);
            if (existingProduct.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé.");
            }

            product.setId(id);
            productDao.update(product);
            return ResponseEntity.ok("Produit mis à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // delete product by id
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

