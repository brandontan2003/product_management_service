package com.example.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductDetailResponse {

    private String productId;
    private String productName;
    private String productDesc;
    private BigDecimal price;

}
