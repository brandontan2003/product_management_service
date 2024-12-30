package com.example.product.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

import static com.example.product.service.constant.ErrorConstant.INVALID_PRODUCT_ID_ERROR;
import static com.example.product.service.constant.ErrorConstant.PRODUCT_ID_EMPTY_ERROR;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProductRequest {

    @NotEmpty(message = PRODUCT_ID_EMPTY_ERROR)
    @UUID(message = INVALID_PRODUCT_ID_ERROR)
    private String productId;

}
