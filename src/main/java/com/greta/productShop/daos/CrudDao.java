package com.greta.productShop.daos;

import com.greta.productShop.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    void save(T entity);
    Optional<T> findById(int id);



    List<T> findAll();
    void update(T entity);
    void deleteById(int id);
}
