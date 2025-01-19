package com.example.product.service.exception;

import com.example.product_common_core.dto.ResponsePayload;
import com.example.product_common_core.dto.error.Error;
import com.example.product_common_core.dto.error.ErrorPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.product_common_core.constant.ApiConstant.STATUS_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ResponsePayload<ErrorPayload>> handleProductException(ProductException ex) {
        ProductErrorMessage err = ex.getErrorMessage();
        return ResponseEntity.status(err.getHttpStatus()).body(ResponsePayload.<ErrorPayload>builder()
                .status(STATUS_ERROR).result(ErrorPayload.builder().error(getError(err)).build()).build());
    }

    private static Error getError(ProductErrorMessage err) {
        return Error.builder().errorCode(err.getErrorCode()).errorMessage(err.getErrorMessage()).build();
    }

}
