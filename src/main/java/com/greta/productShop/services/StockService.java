package com.greta.productShop.services;

import com.greta.productShop.daos.ProductDao;
import com.greta.productShop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private final ProductDao productDao;

    @Autowired
    public StockService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public boolean isStockAvailable(int productId, int quantity) {
        Optional<Product> productOpt = productDao.findById(productId);
        return productOpt.isPresent() && productOpt.get().getQuantity() >= quantity;
    }

    public boolean decreaseStock(int productId, int quantity) {
        Optional<Product> productOpt = productDao.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getQuantity() >= quantity) {
                product.setQuantity(product.getQuantity() - quantity);
                productDao.update(product);
                return true;
            }
        }
        return false;
    }

    public void increaseStock(int productId, int quantity) {
        Optional<Product> productOpt = productDao.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQuantity(product.getQuantity() + quantity);
            productDao.update(product);
        }
    }
}
