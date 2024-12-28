package com.example.product.service.dto.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {

    private String errorCode;
    private String errorMessage;

}
