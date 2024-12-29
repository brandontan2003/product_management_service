package com.example.product.service.dto;

import com.example.product.service.constant.ProductModelConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

import static com.example.product.service.constant.ErrorConstant.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    @NotBlank(message = PRODUCT_ID_EMPTY_ERROR)
    @UUID(message = INVALID_PRODUCT_ID_ERROR)
    private String productId;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_NAME)
    private String productName;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_DESC)
    private String productDesc;
    @Positive(message = PRICE_NEGATIVE_ERROR)
    private BigDecimal price;
}
