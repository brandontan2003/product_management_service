package com.example.product.service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorPayload {

    private String errorCode;
    private String errorMessage;

}
