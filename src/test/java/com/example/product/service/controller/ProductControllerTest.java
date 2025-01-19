package com.example.product.service.controller;

import com.example.product.service.dto.*;
import com.example.product.service.exception.ProductException;
import com.example.product.service.model.Product;
import com.example.product.service.service.ProductService;
import com.example.product_common_core.dto.ResponsePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.product.service.exception.ProductErrorMessage.PRODUCT_NOT_FOUND;
import static com.example.product_common_core.constant.ApiConstant.STATUS_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        RetrieveProductDetailResponse mockProduct = getMockProduct();
        when(productService.retrieveProductDetails(PRODUCT_ID)).thenReturn(mockProduct);

        ResponsePayload<RetrieveProductDetailResponse> actualResponse =
                productController.retrieveProductDetails(PRODUCT_ID);

        assertNotNull(actualResponse);
        assertEquals(STATUS_SUCCESS, actualResponse.getStatus());
        RetrieveProductDetailResponse result = actualResponse.getResult();
        assertEquals(result.getProductId(), mockProduct.getProductId());
        assertEquals(result.getProductName(), mockProduct.getProductName());
        assertEquals(result.getProductDesc(), mockProduct.getProductDesc());
        assertEquals(result.getPrice(), mockProduct.getPrice());
        assertEquals(result.getScheduledDeletionDate(), mockProduct.getScheduledDeletionDate());
    }

    private static RetrieveProductDetailResponse getMockProduct() {
        return new RetrieveProductDetailResponse(PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESC, PRICE, null);
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

    private static CreateProductRequest buildCreateProductRequest() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName(PRODUCT_NAME);
        request.setProductDesc(PRODUCT_DESC);
        request.setPrice(PRICE);
        return request;
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
    void createProduct_Success() {
        CreateProductRequest request = buildCreateProductRequest();
        when(productService.create(request)).thenReturn(getProduct());

        ResponsePayload<CreateProductResponse> actualResponse = productController.createProduct(request);

        assertNotNull(actualResponse);
        assertEquals(STATUS_SUCCESS, actualResponse.getStatus());
        assertNotNull(actualResponse.getResult().getProductId());
    }

    private static UpdateProductRequest buildUpdateProductRequest() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setProductId(PRODUCT_ID);
        request.setProductName(PRODUCT_NAME);
        request.setProductDesc(PRODUCT_DESC);
        request.setPrice(PRICE);
        return request;
    }

    @Test
    void updateProduct_Success() {
        UpdateProductRequest request = buildUpdateProductRequest();
        when(productService.update(request)).thenReturn(getMockProduct());

        ResponsePayload<RetrieveProductDetailResponse> actualResponse = productController.updateProduct(request);

        assertNotNull(actualResponse);
        assertEquals(STATUS_SUCCESS, actualResponse.getStatus());
        RetrieveProductDetailResponse result = actualResponse.getResult();
        assertEquals(PRODUCT_ID, result.getProductId());
        assertEquals(PRODUCT_NAME, result.getProductName());
        assertEquals(PRODUCT_DESC, result.getProductDesc());
        assertEquals(PRICE, result.getPrice());
        assertNull(result.getScheduledDeletionDate());
    }

    @Test
    void updateProduct_productNotFound_Error() {
        UpdateProductRequest request = buildUpdateProductRequest();
        when(productService.update(request)).thenThrow(new ProductException(PRODUCT_NOT_FOUND));
        ProductException ex = assertThrows(ProductException.class, () -> productController.updateProduct(request));

        assertEquals(ex.getErrorMessage(), PRODUCT_NOT_FOUND);
        assertEquals(ex.getErrorMessage().getHttpStatus(), PRODUCT_NOT_FOUND.getHttpStatus());
        assertEquals(ex.getErrorMessage().getErrorCode(), PRODUCT_NOT_FOUND.getErrorCode());
        assertEquals(ex.getErrorMessage().getErrorMessage(), PRODUCT_NOT_FOUND.getErrorMessage());
    }

    @Test
    void deleteProduct_Success() {
        DeleteProductRequest request = new DeleteProductRequest(PRODUCT_ID);
        doNothing().when(productService).delete(request);

        ResponsePayload<String> actualResponse = productController.deleteProduct(request);

        assertNotNull(actualResponse);
        assertEquals(STATUS_SUCCESS, actualResponse.getStatus());
        assertNull(actualResponse.getResult());
    }

    @Test
    void deleteProduct_productNotFound_Error() {
        DeleteProductRequest request = new DeleteProductRequest(PRODUCT_ID);
        doThrow(new ProductException(PRODUCT_NOT_FOUND)).when(productService).delete(request);
        ProductException ex = assertThrows(ProductException.class, () -> productController.deleteProduct(request));

        assertEquals(ex.getErrorMessage(), PRODUCT_NOT_FOUND);
        assertEquals(ex.getErrorMessage().getHttpStatus(), PRODUCT_NOT_FOUND.getHttpStatus());
        assertEquals(ex.getErrorMessage().getErrorCode(), PRODUCT_NOT_FOUND.getErrorCode());
        assertEquals(ex.getErrorMessage().getErrorMessage(), PRODUCT_NOT_FOUND.getErrorMessage());
    }
}
