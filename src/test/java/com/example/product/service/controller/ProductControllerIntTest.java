package com.example.product.service.controller;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.CreateProductResponse;
import com.example.product.service.dto.ResponsePayload;
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

import static com.example.product.service.TestUtils.writeValueAsString;
import static com.example.product.service.constant.UriConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        String expectedResponse = Files.readString(expectedOutput);
        log.info(writeValueAsString(actualResponse));
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void createProduct_Success() throws Exception {
        String actualResponse = mvc.perform(post(API_PRODUCT + API_VERSION_1 + CREATE_URL)
                        .content(writeValueAsString(buildCreateProductRequest(PRODUCT_NAME, PRODUCT_DESC, PRICE)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        log.info("Actual Response: " + writeValueAsString(actualResponse));

        ResponsePayload<CreateProductResponse> responsePayload = new ObjectMapper()
                .readValue(actualResponse, new TypeReference<>() {
                });
        String expectedOutput = Files.readString(basePath.resolve("create_product").resolve(
                "product_created_successfully.json"));
        expectedOutput = expectedOutput.replace("#productId#", responsePayload.getResult().getProductId());
        log.info("Expected Response: " + writeValueAsString(actualResponse));

        JSONAssert.assertEquals(expectedOutput, actualResponse, JSONCompareMode.LENIENT);
    }

    @Test
    void retrieveProductDetails_Success() throws Exception {
        Product product = productRepository.saveAndFlush(productService.create(buildCreateProductRequest(PRODUCT_NAME,
                PRODUCT_DESC, PRICE)));

        String actualResponse = mvc.perform(get(API_PRODUCT + API_VERSION_1 + RETRIEVE_URL + DETAILS_URL
                        + "?productId=" + product.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        log.info("Actual Response: " + writeValueAsString(actualResponse));

        String expectedOutput = Files.readString(basePath.resolve("retrieve_product").resolve(
                "product_retrieved_successfully.json"));
        expectedOutput = expectedOutput.replace("#productId#", product.getProductId());
        log.info("Expected Response: " + writeValueAsString(actualResponse));

        JSONAssert.assertEquals(expectedOutput, actualResponse, JSONCompareMode.LENIENT);
    }
}
