package com.example.product.service.exception;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final ProductErrorMessage errorMessage;

    public ProductException(ProductErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
    }
}
