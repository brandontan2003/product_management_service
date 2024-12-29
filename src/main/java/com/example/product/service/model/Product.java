package com.example.product.service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import static com.example.product.service.constant.ProductModelConstant.*;

@Data
@Entity
@Table(name = PRODUCT_TABLE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = PRODUCT_ID, length = FieldLength.PRODUCT_ID)
    private String productId;
    @Column(name = PRODUCT_NAME, length = FieldLength.PRODUCT_NAME, nullable = false)
    private String productName;
    @Column(name = PRODUCT_DESC, length = FieldLength.PRODUCT_DESC)
    private String productDesc;
    @Column(name = PRICE, nullable = false)
    private BigDecimal price;

}
