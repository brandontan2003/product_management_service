package com.example.product.service.dto;

import com.example.product.service.constant.ProductModelConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static com.example.product.service.constant.ErrorConstant.*;

@Data
@Builder
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = PRODUCT_NAME_EMPTY_ERROR)
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_NAME)
    private String productName;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_DESC)
    private String productDesc;
    @NotNull(message = PRICE_EMPTY_ERROR)
    @Positive(message = PRICE_NEGATIVE_ERROR)
    private BigDecimal price;
}
