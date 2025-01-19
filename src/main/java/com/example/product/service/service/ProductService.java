package com.example.product.service.service;

import com.example.product.service.dto.CreateProductRequest;
import com.example.product.service.dto.DeleteProductRequest;
import com.example.product.service.dto.RetrieveProductDetailResponse;
import com.example.product.service.dto.UpdateProductRequest;
import com.example.product.service.exception.ProductException;
import com.example.product.service.model.Product;
import com.example.product.service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.example.product.service.exception.ProductErrorMessage.PRODUCT_NOT_FOUND;
import static com.example.product_common_core.constant.UtilityConstant.NULL_IDENTIFIER;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;

    @Transactional
    public Product create(CreateProductRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        return productRepository.saveAndFlush(product);
    }

    public RetrieveProductDetailResponse retrieveProductDetails(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        return mapper.map(product, RetrieveProductDetailResponse.class);
    }

    @Transactional
    public RetrieveProductDetailResponse update(UpdateProductRequest request) {
        String productId = request.getProductId();
        Product product = productRepository.findByProductId(productId).orElseThrow(() ->
                new ProductException(PRODUCT_NOT_FOUND));

        productRepository.saveAndFlush(updateCopyProperties(request, product));
        return retrieveProductDetails(productId);
    }

    private Product updateCopyProperties(UpdateProductRequest request, Product product) {
        if (!ObjectUtils.isEmpty(request.getProductName())) {
            product.setProductName(request.getProductName());
        }
        if (!ObjectUtils.isEmpty(request.getProductDesc())) {
            product.setProductDesc(NULL_IDENTIFIER.equalsIgnoreCase(request.getProductDesc()) ? null :
                    request.getProductDesc());
        }
        if (!ObjectUtils.isEmpty(request.getPrice())) {
            product.setPrice(request.getPrice());
        }
        if (!ObjectUtils.isEmpty(request.getScheduledDeletionDate())) {
            product.setScheduledDeletionDate(request.getScheduledDeletionDate());
        }
        return product;
    }

    @Transactional
    public void delete(DeleteProductRequest request) {
        String productId = request.getProductId();
        productRepository.findByProductId(productId).orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        productRepository.deleteById(productId);
    }
}
