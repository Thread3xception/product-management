package example.task.service;


import example.task.exception.EntityNotFoundException;
import example.task.mapper.ProductMapper;
import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import example.task.model.entity.Product;
import example.task.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::mapToProductDto)
                .toList();
    }

    public ProductDto getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::mapToProductDto)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, productId));
    }

    public ProductDto createProduct(CreateProductCommand command) {
        Product entity = productMapper.mapToProductEntity(command);
        productRepository.save(entity);
        return productMapper.mapToProductDto(entity);
    }

    public void deleteById(Long productId) {
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, productId));
        productRepository.delete(entity);
    }
}
