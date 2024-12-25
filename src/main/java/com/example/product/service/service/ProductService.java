package com.example.product.service.service;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public void createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDesc(request.getProductDesc());
        product.setPrice(request.getPrice());

        productRepository.saveAndFlush(product);
    }

}
