package com.example.product.service.dto;

import com.example.product.service.constant.ProductModelConstant;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.example.product.service.constant.ErrorConstant.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = PRODUCT_NAME_EMPTY_ERROR)
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_NAME)
    private String productName;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_DESC)
    private String productDesc;
    @NotNull(message = PRICE_EMPTY_ERROR)
    @PositiveOrZero(message = PRICE_NEGATIVE_ERROR)
    private BigDecimal price;
}
