package example.task.mapper;

import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import example.task.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product mapToProductEntity(CreateProductCommand command);

    ProductDto mapToProductDto(Product entity);
}
