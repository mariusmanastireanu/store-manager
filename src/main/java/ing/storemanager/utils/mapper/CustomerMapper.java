package ing.storemanager.utils.mapper;

import ing.storemanager.domain.Customer;
import ing.storemanager.service.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(config = ObjectMapperConfiguration.class)
public interface CustomerMapper {

    CustomerDTO toDto(Customer user);
    Customer toEntity(CustomerDTO dto);

}
