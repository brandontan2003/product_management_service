package com.example.product.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.product.service.constant.ErrorConstant.*;

@Getter
public enum ProductErrorMessage {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_ERROR_CODE, PRODUCT_NOT_FOUND_ERROR_DESC),
    FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, FIELD_VALIDATION_ERROR_CODE, FIELD_VALIDATION_ERROR_DESC);

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    // Constructor to initialize the enum constants
    ProductErrorMessage(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
