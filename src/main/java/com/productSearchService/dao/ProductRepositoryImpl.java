package com.productSearchService.dao;

import com.productSearchService.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private LuceneDocs luceneDocs;
    public static final String SPLIT_BY = ",";
    public static final String FILE_PATH = "src/main/resources/product.csv";

    List<Product> productList;
    Product product;

    public ProductRepositoryImpl(LuceneDocs luceneDocs) {
        this.luceneDocs = luceneDocs;
    }

    /**
     * This method will load all products from csv file
     * @return
     * @throws Exception
     */
    @Override
    public List<Product> loadProducts() throws Exception {
        productList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));

        String line;
        while ((line = br.readLine()) != null) {
            String[] productAttributes = line.split(SPLIT_BY);
            product = Product.builder()
                    .imageUrl(productAttributes[0])
                    .name(productAttributes[1])
                    .description(productAttributes[2])
                    .price(productAttributes[3])
                    .build();
            productList.add(product);
        }
        return productList;
    }

    /**
     * This method will return all the products on the basis of search query, offset and limit
     * @param keyword
     * @param offset
     * @param limit
     * @return
     * @throws IOException
     */
    @Override
    public List<Product> getAllProducts(String keyword, int offset, int limit) throws IOException {
        return luceneDocs.luceneQuerySearch(keyword, offset, limit);
    }

    /**
     * This method will index products
     * @throws IOException
     */
    @Override
    public void indexProducts() throws IOException {
        luceneDocs.performIndex(productList);
    }
}

