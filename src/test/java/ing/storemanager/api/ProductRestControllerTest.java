package ing.storemanager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.storemanager.service.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddProduct_success() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("MyNewProduct");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("MyNewProduct")))
                .andExpect(jsonPath("$.price", is(2.5d)));
    }

    @Test
    public void testAddProduct_emptyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ProductDTO())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("name")));
    }

    @Test
    public void testAddProduct_sameName() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("ProductWithSameName");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("ProductWithSameName")))
                .andExpect(jsonPath("$.price", is(2.5d)));


        mockMvc.perform(MockMvcRequestBuilders.put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("unique")));
    }

    @Test
    public void testGetProduct_success() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("NewProduct");
        product.setPrice(2.5d);
        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").param("id", insertedDto.getId().toString()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(insertedDto.getId().intValue())));
    }

    @Test
    public void testGetProduct_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").param("id", "15"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
}
