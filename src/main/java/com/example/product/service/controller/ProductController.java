package com.example.product.service.controller;

import com.example.product.service.dto.*;
import com.example.product.service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.product.service.constant.ApiConstant.STATUS_SUCCESS;
import static com.example.product.service.constant.UriConstant.*;

@RestController
@RequestMapping(API_PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(API_VERSION_1 + CREATE_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePayload<CreateProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest createProductRequest) {
        return ResponsePayload.<CreateProductResponse>builder().status(STATUS_SUCCESS).result(CreateProductResponse.builder()
                .productId(productService.create(createProductRequest).getProductId()).build()).build();
    }

    @GetMapping(API_VERSION_1 + RETRIEVE_URL + DETAILS_URL)
    public ResponsePayload<RetrieveProductDetailResponse> retrieveProductDetails(
            @Valid @RequestParam(name = "productId") String productId) {
        return ResponsePayload.<RetrieveProductDetailResponse>builder().status(STATUS_SUCCESS)
                .result(productService.retrieveProductDetails(productId)).build();
    }

    @PutMapping(API_VERSION_1 + UPDATE_URL)
    public ResponsePayload<RetrieveProductDetailResponse> updateProduct(
            @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return ResponsePayload.<RetrieveProductDetailResponse>builder().status(STATUS_SUCCESS)
                .result(productService.update(updateProductRequest)).build();
    }

    @DeleteMapping(API_VERSION_1 + DELETE_URL)
    public ResponsePayload<String> deleteProduct(@Valid @RequestBody DeleteProductRequest deleteProductRequest) {
        productService.delete(deleteProductRequest);
        return ResponsePayload.<String>builder().status(STATUS_SUCCESS).build();
    }
}
