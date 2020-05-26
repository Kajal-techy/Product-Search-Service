package com.productSearchService.config;

import com.productSearchService.dao.LuceneDocs;
import com.productSearchService.dao.ProductRepositoryImpl;
import com.productSearchService.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ServiceInitializer implements CommandLineRunner {

    private ProductRepositoryImpl productRepositoryImpl;
    private LuceneDocs luceneDocs;

    @Autowired
    public ServiceInitializer(ProductRepositoryImpl productRepositoryImpl, LuceneDocs luceneDocs) {
        this.productRepositoryImpl = productRepositoryImpl;
        this.luceneDocs = luceneDocs;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Initialization of service. Loading products and indexing.");
        List<Product> productList = productRepositoryImpl.loadProducts();
        productList = productList.stream()
                .sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
        luceneDocs.performIndex(productList);
    }
}
