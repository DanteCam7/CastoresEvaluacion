package com.castores.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.castores.inventario") 
public class InventarioApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventarioApplication.class, args);
    }
}