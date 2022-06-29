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

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        product.setName("MyNewProduct");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ProductDTO())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("name")));
    }

    @Test
    public void testAddProduct_sameName() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("ProductWithSameName");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("ProductWithSameName")))
                .andExpect(jsonPath("$.price", is(2.5d)));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("unique")));
    }

    @Test
    public void testGetProduct_success() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("NewProduct");
        product.setPrice(2.5d);
        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").param("id", "583840"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Collection<ProductDTO> result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.get("/api/product/all"))
                .andReturn()
                .getResponse()
                .getContentAsString(), Collection.class);
        int currentSize = result.size();

        ProductDTO product = new ProductDTO();
        product.setName("P1");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("P1")))
                .andExpect(jsonPath("$.price", is(2.5d)));

        ProductDTO product2 = new ProductDTO();
        product2.setName("P2");
        product2.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product2)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("P2")))
                .andExpect(jsonPath("$.price", is(2.5d)));

        result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.get("/api/product/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), Collection.class);
        assertEquals(currentSize + 2, result.size());
    }

    @Test
    public void testUpdateProduct_success() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("UpdateMe");
        product.setPrice(2.5d);
        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductDTO.class);

        product.setId(insertedDto.getId());
        product.setName("NewName");
        product.setPrice(3d);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", containsString("NewName")))
                .andExpect(jsonPath("$.price", is(3d)));
    }

    @Test
    public void testUpdateProduct_ProductNotFound() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("UpdateMe");
        product.setPrice(2.5d);
        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductDTO.class);

        product.setId(99999L);
        product.setName("NewName");
        product.setPrice(3d);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateProduct_DuplicateNames() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("Update1");
        product.setPrice(2.5d);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", containsString("Update1")))
                .andExpect(jsonPath("$.price", is(2.5d)));

        product.setName("Update2");
        product.setPrice(2.5d);
        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductDTO.class);

        product.setId(insertedDto.getId());
        product.setName("Update1");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void removeProduct_success() throws Exception {
        Collection<ProductDTO> result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.get("/api/product/all"))
                .andReturn()
                .getResponse()
                .getContentAsString(), Collection.class);
        int currentSize = result.size();

        ProductDTO product = new ProductDTO();
        product.setName("Update1");
        product.setPrice(2.5d);

        ProductDTO insertedDto = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", containsString("Update1")))
                .andExpect(jsonPath("$.price", is(2.5d)))
                .andReturn().getResponse()
                .getContentAsString(), ProductDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product")
                        .param("id", insertedDto.getId().toString()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        result = new ObjectMapper().readValue(mockMvc.perform(MockMvcRequestBuilders.get("/api/product/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andReturn()
                .getResponse()
                .getContentAsString(), Collection.class);
        assertEquals(currentSize, result.size());
    }

    @Test
    public void removeProduct_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product")
                        .param("id", "999412"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}
