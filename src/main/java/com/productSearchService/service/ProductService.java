package com.productSearchService.service;

import com.productSearchService.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public List<Product> getAllProducts(String keyword, int offset, int limit) throws IOException;

    public void indexProducts() throws IOException ;
}
