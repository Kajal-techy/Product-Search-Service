package com.productSearchService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    String imageUrl;
    String name;
    String description;
    String price;
}
