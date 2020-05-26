package com.productSearchService.service;

import com.productSearchService.dao.ProductRepository;
import com.productSearchService.model.Product;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts(String keyword, int offset, int limit) throws IOException {
        return productRepository.getAllProducts(keyword, offset, limit);
    }

    @Override
    public void indexProducts() throws IOException {
        productRepository.indexProducts();
    }
}
