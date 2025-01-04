package com.example.product.service.dto;

import com.example.product.service.constant.ProductModelConstant;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.product.service.constant.ErrorConstant.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    @NotEmpty(message = PRODUCT_ID_EMPTY_ERROR)
    @UUID(message = INVALID_PRODUCT_ID_ERROR)
    private String productId;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_NAME)
    private String productName;
    @Size(max = ProductModelConstant.FieldLength.PRODUCT_DESC)
    private String productDesc;
    @PositiveOrZero(message = PRICE_NEGATIVE_ERROR)
    private BigDecimal price;
    @Future(message = SCHEDULED_DATE_NOT_FUTURE_ERROR)
    private LocalDate scheduledDeletionDate;
}
