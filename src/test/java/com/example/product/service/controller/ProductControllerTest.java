package com.example.product.service.controller;

import com.example.product.service.dto.ResponsePayload;
import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.exception.ProductErrorMessage;
import com.example.product.service.exception.ProductErrorMessage.*;
import com.example.product.service.exception.ProductException;
import com.example.product.service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.product.service.constant.ApiConstant.STATUS_SUCCESS;
import static com.example.product.service.exception.ProductErrorMessage.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private static final String PRODUCT_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_NAME = "Sample Product";
    private static final String PRODUCT_DESC = "Sample Product Description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);

    @Test
    void retrieveProductDetails_ReturnExistingProduct_Success() {
        RetrieveProductDetailResponse mockProduct = new RetrieveProductDetailResponse(PRODUCT_ID, PRODUCT_NAME,
                PRODUCT_DESC, PRICE);
        when(productService.retrieveProductDetails(PRODUCT_ID)).thenReturn(mockProduct);

        ResponsePayload<RetrieveProductDetailResponse> actualResponse = productController.retrieveProductDetails(PRODUCT_ID);

        assertNotNull(actualResponse);
        assertEquals(STATUS_SUCCESS, actualResponse.getStatus());
        RetrieveProductDetailResponse result = actualResponse.getResult();
        assertEquals(result.getProductId(), mockProduct.getProductId());
        assertEquals(result.getProductName(), mockProduct.getProductName());
        assertEquals(result.getProductDesc(), mockProduct.getProductDesc());
        assertEquals(result.getPrice(), mockProduct.getPrice());
    }

    @Test
    void retrieveProductDetails_ProductNotFound_Error() {
        when(productService.retrieveProductDetails(PRODUCT_ID)).thenThrow(new ProductException(PRODUCT_NOT_FOUND));


        ProductException ex = assertThrows(ProductException.class, () -> productController
                .retrieveProductDetails(PRODUCT_ID));

        assertEquals(ex.getErrorMessage(), PRODUCT_NOT_FOUND);
        assertEquals(ex.getErrorMessage().getHttpStatus(), PRODUCT_NOT_FOUND.getHttpStatus());
        assertEquals(ex.getErrorMessage().getErrorCode(), PRODUCT_NOT_FOUND.getErrorCode());
        assertEquals(ex.getErrorMessage().getErrorMessage(), PRODUCT_NOT_FOUND.getErrorMessage());
    }
}
