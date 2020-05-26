package com.productSearchService.controller;

import com.productSearchService.model.Product;
import com.productSearchService.service.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * This method will do indexing
     * @param index
     * @return
     * @throws IOException
     */
    @GetMapping("/product/{index}")
    public ResponseEntity<Void> getProductByIndex(@PathVariable String index) throws IOException {
        log.info("Entering UserController.getUserDetailsById with parameter index {}", index);
        productServiceImpl.indexProducts();
        log.info("Exiting from ProductController.ResponseEntity");
        return ResponseEntity.noContent().build();
    }


    /**
     * This method will search the products on the basis of q, offset and limit
     * @param q
     * @param offset
     * @param limit
     * @return
     * @throws IOException
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> getProductsSearch(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int offset,
                                               @RequestParam int limit) throws IOException {
        log.info("Entering UserController.getUserDetailsById with parameter q {}, offset {} and limit {}.", q, offset, limit);
        List<Product> productList = productServiceImpl.getAllProducts(q, offset, limit);
        log.info("Exiting from ProductController.getProductsSearch with return value productList {}", productList);
        return ResponseEntity.ok().body(productList);
    }
}
