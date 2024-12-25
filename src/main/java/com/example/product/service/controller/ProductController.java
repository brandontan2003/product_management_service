package com.example.product.service.controller;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.product.service.constant.UriConstant.*;

@RestController
@RequestMapping(API_PRODUCT)
public class ProductController {

    private ProductService productService;

    @PostMapping(API_VERSION_1 + CREATE_URL)
    public String createProduct(@RequestBody CreateProductRequest createProductRequest) {
//        productService.createProduct(createProductRequest);
        return "Test";
    }
}
