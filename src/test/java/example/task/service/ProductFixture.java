package example.task.service;

import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import example.task.model.entity.Product;

public interface ProductFixture {

    String PRODUCT_NAME = "example";
    Double PRODUCT_PRICE = 10000.0;


    default Product buildProduct() {
        return Product.builder()
                .id(1L)
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();
    }

    default CreateProductCommand buildCommand() {
        return CreateProductCommand.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();
    }

    default ProductDto buildProductDto() {
        return ProductDto.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();
    }
}
