package example.task.service;

import example.task.exception.EntityNotFoundException;
import example.task.mapper.ProductMapper;
import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import example.task.model.entity.Product;
import example.task.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest implements ProductFixture {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;


    @DisplayName("Should get single product")
    @Test
    void shouldGetSingleProduct() {
        // given
        Long id = 1L;
        Product product = buildProduct();
        ProductDto productDto = buildProductDto();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        //when
        ProductDto foundProduct = productService.getProductById(id);

        //then
        assertEquals(foundProduct.getName(), product.getName());
        assertEquals(foundProduct.getPrice(), product.getPrice());
    }

    @DisplayName("Should save product")
    @Test
    void shouldSaveProduct() {
        // given
        Product product = buildProduct();
        CreateProductCommand command = buildCommand();
        ProductDto productDto = buildProductDto();

        when(productMapper.mapToProductEntity(command)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        //when
        ProductDto savedProduct = productService.createProduct(command);

        //then
        verify(productRepository, times(1)).save(product);
        assertEquals(savedProduct.getName(), product.getName());
        assertEquals(savedProduct.getPrice(), product.getPrice());
    }

    @DisplayName("Should delete product")
    @Test
    void shouldDeleteProduct() {
        // given
        Long id = 1L;
        Product product = buildProduct();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // when
        productService.deleteById(id);

        // then
        verify(productRepository, times(1)).delete(product);
    }

    @DisplayName("Should throw exception when delete product but not found")
    @Test
    void shouldThrowExceptionWhenDeleteProduct() {
        // given
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> productService.deleteById(id));

        // then
        assertEquals("Product with id: 1 not found!", ex.getMessage());
    }
}