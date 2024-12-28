package com.example.product.service.service;

import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.exception.ProductErrorMessage;
import com.example.product.service.exception.ProductException;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final String PRODUCT_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_NAME = "Sample Product";
    private static final String PRODUCT_DESC = "Sample Product Description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productService, "mapper", new ModelMapper());
    }

    @Test
    void retrieveProductDetails_ReturnExistingProduct_Success() {
        Product mockProduct = new Product();
        mockProduct.setProductId(PRODUCT_ID);
        mockProduct.setProductName(PRODUCT_NAME);
        mockProduct.setProductDesc(PRODUCT_DESC);
        mockProduct.setPrice(PRICE);
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(mockProduct));

        RetrieveProductDetailResponse actualResponse = productService.retrieveProductDetails(PRODUCT_ID);

        verify(productRepository, times(1)).findByProductId(PRODUCT_ID);
        assertNotNull(actualResponse);
        assertEquals(actualResponse.getProductId(), mockProduct.getProductId());
        assertEquals(actualResponse.getProductName(), mockProduct.getProductName());
        assertEquals(actualResponse.getProductDesc(), mockProduct.getProductDesc());
        assertEquals(actualResponse.getPrice(), mockProduct.getPrice());
    }

    @Test
    void retrieveProductDetails_ProductNotFound_Error() {
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());

        ProductException ex = assertThrows(ProductException.class, () -> productService
                .retrieveProductDetails(PRODUCT_ID));

        assertEquals(ex.getErrorMessage(), ProductErrorMessage.PRODUCT_NOT_FOUND);
        assertEquals(ex.getErrorMessage().getHttpStatus(), ProductErrorMessage.PRODUCT_NOT_FOUND.getHttpStatus());
        assertEquals(ex.getErrorMessage().getErrorCode(), ProductErrorMessage.PRODUCT_NOT_FOUND.getErrorCode());
        assertEquals(ex.getErrorMessage().getErrorDesc(), ProductErrorMessage.PRODUCT_NOT_FOUND.getErrorDesc());
    }

}
