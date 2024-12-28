package com.example.product.service.exception;

import com.example.product.service.dto.ResponsePayload;
import com.example.product.service.dto.error.Error;
import com.example.product.service.dto.error.ErrorPayload;
import com.example.product.service.dto.error.ErrorsPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.example.product.service.constant.ApiConstant.STATUS_ERROR;
import static com.example.product.service.exception.ProductErrorMessage.FIELD_VALIDATION_ERROR;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponsePayload<ErrorsPayload>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorDescriptions = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        List<Error> errorList = new ArrayList<>();
        errorDescriptions.forEach(errorDescription -> {
            Error err = getError(FIELD_VALIDATION_ERROR);
            err.setErrorMessage(errorDescription);
            errorList.add(err);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayload.<ErrorsPayload>builder()
                .status(STATUS_ERROR).result(ErrorsPayload.builder().errors(errorList).build()).build());
    }
}
