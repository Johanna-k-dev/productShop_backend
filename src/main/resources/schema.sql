CREATE DATABASE IF NOT EXISTS product_shop;
USE product_shop;

CREATE TABLE IF NOT EXISTS user (
 id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(50) NOT NULL,
 first_name VARCHAR(50) NOT NULL,
 email VARCHAR(100) NOT NULL UNIQUE,
 address VARCHAR(200) NOT NULL,
 postal_number VARCHAR(50) NOT NULL,
 phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    price DOUBLE(10,2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS invoice (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    date             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id          INT NOT NULL,
    product_id       INT NOT NULL,
    order_status     VARCHAR(200) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
