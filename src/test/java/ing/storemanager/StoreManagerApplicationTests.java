package ing.storemanager;

import ing.storemanager.api.ProductRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StoreManagerApplicationTests {

	@Autowired
	private ProductRestController productRestController;

	@Test
	void contextLoads() {
		assertNotNull(productRestController);
	}

}
