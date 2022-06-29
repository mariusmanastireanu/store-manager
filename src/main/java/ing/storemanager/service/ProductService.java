package ing.storemanager.service;

import ing.storemanager.domain.Product;
import ing.storemanager.repository.ProductRepository;
import ing.storemanager.service.dto.ProductDTO;
import ing.storemanager.utils.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductRepository repository;

    public ProductDTO addProduct(ProductDTO product) {
        return mapper.toDto(repository.save(mapper.toEntity(product)));
    }

    public ProductDTO getProduct(Long id) {
        final Optional<Product> optional = repository.findById(id);
        if (optional.isEmpty()) {
            // TODO - throw not found ex
            return null;
        }
        return mapper.toDto(optional.get());
    }
}
