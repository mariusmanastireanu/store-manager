package ing.storemanager.service;

import ing.storemanager.domain.Product;
import ing.storemanager.error.BadRequestException;
import ing.storemanager.error.EntityNotFoundException;
import ing.storemanager.repository.ProductRepository;
import ing.storemanager.service.dto.ProductDTO;
import ing.storemanager.utils.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductRepository repository;

    public ProductDTO addProduct(ProductDTO product) {
        if (product == null) {
            throw new BadRequestException("No product was added");
        } else if (product.getName() == null) {
            throw new BadRequestException("Product name is mandatory.", "name", null);
        } else if (repository.findByName(product.getName()).isPresent()) {
            throw new BadRequestException("Product must have unique names. A product with this name already exists", "name", product.getName());
        }
        return mapper.toDto(repository.save(mapper.toEntity(product)));
    }

    public ProductDTO getProduct(Long id) {
        final Optional<Product> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException(ProductDTO.class, "id", id.toString());
        }
        return mapper.toDto(optional.get());
    }

    public Collection<ProductDTO> getAllProducts() {
        final List<ProductDTO> result = new ArrayList<>();
        repository.findAll().forEach(product -> result.add(mapper.toDto(product)));
        return result;
    }

    public ProductDTO updateProduct(ProductDTO product) {
        if (repository.findById(product.getId()).isEmpty()) {
            throw new EntityNotFoundException(ProductDTO.class, "id", product.getId().toString());
        }
        if (repository.findByName(product.getName()).isPresent()
                && !Objects.equals(product.getId(), repository.findByName(product.getName()).get().getId())) {
            throw new BadRequestException("Product must have unique names. A product with this name already exists", "name", product.getName());
        }
        return mapper.toDto(repository.save(mapper.toEntity(product)));
    }
}
