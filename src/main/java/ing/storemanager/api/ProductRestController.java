package ing.storemanager.api;

import ing.storemanager.service.ProductService;
import ing.storemanager.service.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PutMapping
    public ProductDTO addProduct(@RequestBody ProductDTO product) {
        return productService.addProduct(product);
    }

    @GetMapping
    public ProductDTO getProduct(@RequestParam Long id) {
        return productService.getProduct(id);
    }

    @GetMapping(path = "/all")
    public Collection<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

}
