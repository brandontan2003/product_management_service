package com.example.product.service.dto.error;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorsPayload {

    private List<Error> errors;

}
