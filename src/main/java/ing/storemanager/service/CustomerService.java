package ing.storemanager.service;

import ing.storemanager.domain.Customer;
import ing.storemanager.error.BadRequestException;
import ing.storemanager.repository.CustomerRepository;
import ing.storemanager.service.dto.CustomerDTO;
import ing.storemanager.utils.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerMapper mapper;

    public CustomerDTO registerCustomer(String email, String password) {
        if (customerRepository.findById(email).isPresent()) {
            throw new BadRequestException("A customer with this email is already registered.", "email", email);
        }
        final Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        return mapper.toDto(customerRepository.save(customer));
    }

}
