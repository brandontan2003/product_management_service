package com.example.product.service.service;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.exception.ProductErrorMessage;
import com.example.product.service.exception.ProductException;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataInput;
import java.io.IOException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;

    public Product create(CreateProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDesc(request.getProductDesc());
        product.setPrice(request.getPrice());

        return productRepository.saveAndFlush(product);
    }

    public RetrieveProductDetailResponse retrieveProductDetails(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(ProductErrorMessage.PRODUCT_NOT_FOUND));
        return mapper.map(product, RetrieveProductDetailResponse.class);
    }
}
