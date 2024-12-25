package com.example.product.service.dto;

import com.example.product.service.constant.ProductModelConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_NAME)
    private String productName;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_DESC)
    private String productDesc;
    @NotBlank
    private BigDecimal price;
}
