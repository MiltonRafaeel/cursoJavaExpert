package com.devsuperior.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String adminToken, clientToken, invalidToken;
	private Long existingProductId, nonExistingProductId, dependentProducId;
		
	private String productName;
	
	private Product product;
	private ProductDTO productDto;
	
	@BeforeEach
	void setUp() throws Exception {
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		existingProductId = 2L;
		nonExistingProductId = 100L;
		dependentProducId = 3L;
		
		productName = "Macbook";
		
		Category category = new Category(2L, "Eletro");
		product = new Product(null, "Console Playstation 5", "Lorem ipsum, dolor...", 3999.99, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
		product.getCategories().add(category);
		
		productDto = new ProductDTO(product);
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		invalidToken = adminToken + "xpto";

	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/products?name={productName}", productName)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));		
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/products")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));	
		
		result.andExpect(jsonPath("$.content[1].id").value(2L));
		result.andExpect(jsonPath("$.content[1].name").value("Smart TV"));
		result.andExpect(jsonPath("$.content[1].price").value(2190.0));
		result.andExpect(jsonPath("$.content[1].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"));	
	}
	
	@Test
	public void insertShouldReturnProductDTOCreatedWhenAdminLogged() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L));
		result.andExpect(jsonPath("$.name").value("Console Playstation 5"));
		result.andExpect(jsonPath("$.description").value("Lorem ipsum, dolor..."));
		result.andExpect(jsonPath("$.price").value(3999.99));
		result.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		result.andExpect(jsonPath("$.categories[0].id").value(2L));
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {
		
		product.setName("ab");
		productDto = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {
		
		product.setDescription("ab");
		productDto = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {
		
		product.setPrice(-50.0);
		productDto = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {
		
		product.setPrice(0.0);
		productDto = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProducthAsNotCategory() throws Exception {
		
		product.getCategories().clear();
		productDto = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnForbbidenWhenClientLogged() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + clientToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + invalidToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenDoesNotHaveCategoryAndAdminLogged() throws Exception { // esse meu teste ja existe, entretanto fiz dessa outra maneira para manter minha fixacao
		
		productDto = new ProductDTO(null, "teste", "test insert", 62.0, "https://testInsert.com");
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExistAndAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdExistAndAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", nonExistingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteShouldReturnBadRequestWhenIdExistAndAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", dependentProducId)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenClientLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenExistIdAndInvalidToken() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", nonExistingProductId)
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void updateShouldReturnOkWhenAdminLogged() throws Exception {
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.name").value("Console Playstation 5"));
		result.andExpect(jsonPath("$.description").value("Lorem ipsum, dolor..."));
		result.andExpect(jsonPath("$.price").value(3999.99));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenAdminLogged() throws Exception {
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", nonExistingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenDoesNotHaveValidFieldNameAndAdminLogged() throws Exception {
		
		product.setName("a");
		productDto = new ProductDTO(product);
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenDoesNotHaveValidFieldDescriptionAndAdminLogged() throws Exception {
		
		product.setDescription("test desc");
		productDto = new ProductDTO(product);
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenPriceIsNegativeAndAdminLogged() throws Exception {
		
		product.setPrice(-3999.99);
		productDto = new ProductDTO(product);
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenPriceIsZeroAndAdminLogged() throws Exception {
		
		product.setPrice(0.0);
		productDto = new ProductDTO(product);
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void updateShouldReturnUnprocessableEntityWhenDoesNotHaveCategoryAndAdminLogged() throws Exception {
		
		product.getCategories().clear();
		productDto = new ProductDTO(product);
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void updateShouldReturnForbiddenWhenClientLogged() throws Exception {
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + clientToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
				
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc
				.perform(put("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + invalidToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
