package com.greta.productShop.services;

import com.greta.productShop.daos.UserDao;
import com.greta.productShop.entity.User;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
