package ing.storemanager.api;

import ing.storemanager.service.CustomerService;
import ing.storemanager.service.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public CustomerDTO registerCustomer(@RequestParam String email, @RequestParam String password) {
        return customerService.registerCustomer(email, password);
    }

}
