package com.greta.productShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication(scanBasePackages = "com.greta.productShop")
public class ProductShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductShopApplication.class, args);
	}

	@GetMapping("/")
	public String h1() {
		return "ProductShop Backend";
	}
}
