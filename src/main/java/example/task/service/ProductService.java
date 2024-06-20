package example.task.service;

import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllProducts(Pageable pageable);

    ProductDto getProductById(Long productId);

    ProductDto createProduct(CreateProductCommand command);

    void deleteById(Long productId);
}
