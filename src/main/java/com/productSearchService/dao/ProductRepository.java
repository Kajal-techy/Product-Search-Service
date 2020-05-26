package com.productSearchService.dao;

import com.productSearchService.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {

    public List<Product> loadProducts() throws Exception;

    public List<Product> getAllProducts(String keyword, int offset, int limit) throws IOException;

    public void indexProducts() throws IOException;
}
