package com.example.product.service.repository;

import com.example.product.service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByProductId(String productId);

}
