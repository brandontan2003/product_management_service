package com.example.product.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePayload<T> {

    private String status;
    private T result;
}
