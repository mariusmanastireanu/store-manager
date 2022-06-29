package ing.storemanager.utils.mapper;

import ing.storemanager.domain.Product;
import ing.storemanager.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(config = ObjectMapperConfiguration.class)
public interface ProductMapper {

    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO productDTO);

}
