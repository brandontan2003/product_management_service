package com.example.product.service.exception;

import com.example.product.service.dto.ErrorPayload;
import com.example.product.service.dto.ResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.product.service.constant.ApiConstant.STATUS_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ResponsePayload<ErrorPayload>> handleProductException(ProductException ex) {
        ProductErrorMessage err = ex.getErrorMessage();
        return ResponseEntity.status(err.getHttpStatus()).body(ResponsePayload.<ErrorPayload>builder()
                .status(STATUS_ERROR).result(getErrorPayload(err)).build());
    }

    private static ErrorPayload getErrorPayload(ProductErrorMessage err) {
        return ErrorPayload.builder().errorCode(err.getErrorCode()).errorMessage(err.getErrorDesc()).build();
    }

}
