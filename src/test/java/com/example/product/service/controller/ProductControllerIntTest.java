package com.example.product.service.controller;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.CreateProductResponse;
import com.example.product.service.dto.ResponsePayload;
import com.example.product.service.dto.UpdateProductRequest;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import com.example.product.service.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static com.example.product.service.TestUtils.*;
import static com.example.product.service.constant.UriConstant.*;
import static com.example.product.service.constant.UtilityConstant.NULL_IDENTIFIER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private static final String PRODUCT_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_NAME = "Sample Product";
    private static final String PRODUCT_DESC = "Sample Product Description";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);
    private static final Path basePath = Paths.get("src", "test", "resources", "expected_output");

    private static CreateProductRequest buildCreateProductRequest(String productName, String productDesc,
                                                                  BigDecimal price) {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName(productName);
        request.setProductDesc(productDesc);
        request.setPrice(price);
        return request;
    }

    private static UpdateProductRequest buildUpdateProductRequest(String productId, String productDesc,
                                                                  BigDecimal price) {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setProductId(productId);
        request.setProductName(ProductControllerIntTest.PRODUCT_NAME);
        request.setProductDesc(productDesc);
        request.setPrice(price);
        return request;
    }

    private Product saveProduct() {
        Product product = new Product();
        product.setProductName(PRODUCT_NAME);
        product.setProductDesc(PRODUCT_DESC);
        product.setPrice(PRICE);
        return productRepository.saveAndFlush(product);
    }

    static Stream<Arguments> test_createProduct_Failure() {
        Path createProduct = basePath.resolve("create_product");
        return Stream.of(
                Arguments.of("Missing productName", buildCreateProductRequest(null, null, PRICE),
                        createProduct.resolve("missing_productName_error.json")),
                Arguments.of("Missing productName", buildCreateProductRequest(null, PRODUCT_DESC, null),
                        createProduct.resolve("missing_mandatoryFields_error.json")),
                Arguments.of("Missing price", buildCreateProductRequest(PRODUCT_NAME, PRODUCT_DESC, null),
                        createProduct.resolve("missing_price_error.json")),
                Arguments.of("Price is negative", buildCreateProductRequest(PRODUCT_NAME, PRODUCT_DESC,
                                BigDecimal.valueOf(-1.0)),
                        createProduct.resolve("missing_priceNegative_error.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("test_createProduct_Failure")
    void createProduct_Failure(String name, CreateProductRequest request, Path expectedOutput) throws Exception {
        String actualResponse = mvc.perform(post(API_PRODUCT + API_VERSION_1 + CREATE_URL)
                        .content(writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        String expectedResponse = Files.readString(expectedOutput);
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void createProduct_Success() throws Exception {
        String actualResponse = mvc.perform(post(API_PRODUCT + API_VERSION_1 + CREATE_URL)
                        .content(writeValueAsString(buildCreateProductRequest(PRODUCT_NAME, PRODUCT_DESC, PRICE)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        ResponsePayload<CreateProductResponse> responsePayload = new ObjectMapper()
                .readValue(actualResponse, new TypeReference<>() {
                });
        String expectedResponse = Files.readString(basePath.resolve("create_product").resolve(
                "product_created_success.json"));
        expectedResponse = expectedResponse.replace("#productId#", responsePayload.getResult().getProductId());
        log.info(EXPECTED_RESPONSE + writeValueAsString(actualResponse));

        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void retrieveProductDetails_Success() throws Exception {
        Product product = saveProduct();
        String actualResponse = mvc.perform(get(API_PRODUCT + API_VERSION_1 + RETRIEVE_URL + DETAILS_URL
                        + "?productId=" + product.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        String expectedResponse = Files.readString(basePath.resolve("retrieve_product").resolve(
                "product_retrieved_success.json"));
        expectedResponse = expectedResponse.replace("#productId#", product.getProductId());
        log.info(EXPECTED_RESPONSE + writeValueAsString(actualResponse));

        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    static Stream<Arguments> test_retrieveProductDetails_Failure() {
        Path retrieveProduct = basePath.resolve("retrieve_product");
        return Stream.of(
                Arguments.of("Product Not Found", "?productId=1234", retrieveProduct.resolve(
                        "product_notFound_error.json")),
                Arguments.of("Request Param productId is missing", "", retrieveProduct.resolve(
                        "missing_requestParams_error.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("test_retrieveProductDetails_Failure")
    void retrieveProductDetails_Failure(String name, String requestParams, Path expectedOutput) throws Exception {
        String actualResponse =
                mvc.perform(get(API_PRODUCT + API_VERSION_1 + RETRIEVE_URL + DETAILS_URL + requestParams)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        String expectedResponse = Files.readString(expectedOutput);
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    static Stream<Arguments> test_updateProduct_Failure() {
        Path updateProduct = basePath.resolve("update_product");
        return Stream.of(
                Arguments.of("Product not found", buildUpdateProductRequest(PRODUCT_ID, PRODUCT_DESC,
                        PRICE), updateProduct.resolve("product_notFound_error.json")),
                Arguments.of("Product Id is missing", buildUpdateProductRequest(null, PRODUCT_DESC,
                        PRICE), updateProduct.resolve("missing_productId_error.json")),
                Arguments.of("Product Id and Price is invalid", buildUpdateProductRequest("1234",
                        PRODUCT_DESC, BigDecimal.valueOf(-1.0)), updateProduct.resolve(
                        "invalid_productId_price_error.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("test_updateProduct_Failure")
    void updateProduct_Failure(String name, UpdateProductRequest request, Path expectedOutput) throws Exception {
        String actualResponse =
                mvc.perform(put(API_PRODUCT + API_VERSION_1 + UPDATE_URL)
                                .content(writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        String expectedResponse = Files.readString(expectedOutput);
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    static Stream<Arguments> test_updateProduct_Success() {
        Path updateProduct = basePath.resolve("update_product");
        return Stream.of(
                Arguments.of("Product updated successfully", buildUpdateProductRequest(PRODUCT_ID,
                        PRODUCT_DESC, PRICE), updateProduct.resolve("product_updated_success.json")),
                Arguments.of("Update product set fields to null successfully", buildUpdateProductRequest(PRODUCT_ID,
                        NULL_IDENTIFIER, BigDecimal.valueOf(0)), updateProduct.resolve(
                        "product_nullFields_success.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("test_updateProduct_Success")
    void updateProduct_Success(String name, UpdateProductRequest request, Path expectedOutput) throws Exception {
        Product product = saveProduct();
        String savedProductId = product.getProductId();
        request.setProductId(savedProductId);

        String actualResponse =
                mvc.perform(put(API_PRODUCT + API_VERSION_1 + UPDATE_URL)
                                .content(writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString();
        log.info(ACTUAL_RESPONSE + writeValueAsString(actualResponse));

        String expectedResponse = Files.readString(expectedOutput);
        expectedResponse = expectedResponse.replace("#productId#", savedProductId);
        log.info(EXPECTED_RESPONSE + writeValueAsString(expectedResponse));

        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }
}
