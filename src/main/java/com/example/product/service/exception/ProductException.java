package com.example.product.service.exception;

import com.example.product.service.dto.ErrorPayload;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {

    private final ProductErrorMessage errorMessage;

    public ProductException(ProductErrorMessage errorMessage) {
        super(errorMessage.getErrorDesc());
        this.errorMessage = errorMessage;
    }
}
