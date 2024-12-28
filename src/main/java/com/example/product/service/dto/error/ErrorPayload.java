package com.example.product.service.dto.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorPayload {

    private Error error;

}
