package com.example.product.service.controller;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.CreateProductResponse;
import com.example.product.service.dto.ResponsePayload;
import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.service.ProductService;
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
            @RequestBody CreateProductRequest createProductRequest) {
        return ResponsePayload.<CreateProductResponse>builder().status(STATUS_SUCCESS).result(CreateProductResponse.builder()
                .productId(productService.create(createProductRequest).getProductId()).build()).build();
    }

    @GetMapping(API_VERSION_1 + RETRIEVE_URL + DETAILS_URL)
    public ResponsePayload<RetrieveProductDetailResponse> retrieveProductDetails(
            @RequestParam(name = "productId") String productId) {
        return ResponsePayload.<RetrieveProductDetailResponse>builder().status(STATUS_SUCCESS)
                .result(productService.retrieveProductDetails(productId)).build();
    }
}
