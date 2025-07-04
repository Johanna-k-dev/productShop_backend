package com.greta.productShop.daos;

import java.util.List;
import java.util.Optional;


public interface CrudDao<T> {
    boolean save(T entity);
    Optional<T> findById(int id);
    void update(T entity);
    void deleteById(int id);
    List<T> findAll();
}



