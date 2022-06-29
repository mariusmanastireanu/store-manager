package ing.storemanager.repository;

import ing.storemanager.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository  extends CrudRepository<Product, Long> {

}
