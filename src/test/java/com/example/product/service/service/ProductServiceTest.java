package com.example.product.service.service;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.exception.ProductErrorMessage;
import com.example.product.service.exception.ProductException;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
        Product mockProduct = getProduct();
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(mockProduct));

        RetrieveProductDetailResponse actualResponse = productService.retrieveProductDetails(PRODUCT_ID);

        verify(productRepository, times(1)).findByProductId(PRODUCT_ID);
        assertNotNull(actualResponse);
        assertEquals(actualResponse.getProductId(), mockProduct.getProductId());
        assertEquals(actualResponse.getProductName(), mockProduct.getProductName());
        assertEquals(actualResponse.getProductDesc(), mockProduct.getProductDesc());
        assertEquals(actualResponse.getPrice(), mockProduct.getPrice());
    }

    private static Product getProduct() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        product.setProductName(PRODUCT_NAME);
        product.setProductDesc(PRODUCT_DESC);
        product.setPrice(PRICE);
        return product;
    }

    @Test
    void retrieveProductDetails_ProductNotFound_Error() {
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());

        ProductException ex = assertThrows(ProductException.class, () -> productService
                .retrieveProductDetails(PRODUCT_ID));

        assertEquals(ex.getErrorMessage(), ProductErrorMessage.PRODUCT_NOT_FOUND);
        assertEquals(ex.getErrorMessage().getHttpStatus(), ProductErrorMessage.PRODUCT_NOT_FOUND.getHttpStatus());
        assertEquals(ex.getErrorMessage().getErrorCode(), ProductErrorMessage.PRODUCT_NOT_FOUND.getErrorCode());
        assertEquals(ex.getErrorMessage().getErrorMessage(), ProductErrorMessage.PRODUCT_NOT_FOUND.getErrorMessage());
    }

    private static CreateProductRequest buildCreateProductRequest() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName(PRODUCT_NAME);
        request.setProductDesc(PRODUCT_DESC);
        request.setPrice(PRICE);
        return request;
    }

    @Test
    void createProduct_Success() {
        CreateProductRequest request = buildCreateProductRequest();
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(getProduct());

        Product actualResponse = productService.create(request);

        assertNotNull(actualResponse);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).saveAndFlush(captor.capture());
        Product capturedProduct = captor.getValue();
        assertEquals(request.getProductName(), capturedProduct.getProductName());
        assertEquals(request.getProductDesc(), capturedProduct.getProductDesc());
        assertEquals(request.getPrice(), capturedProduct.getPrice());
    }

}
