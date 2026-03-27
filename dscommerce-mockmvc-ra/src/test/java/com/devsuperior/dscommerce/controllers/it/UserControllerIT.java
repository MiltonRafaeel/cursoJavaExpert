package com.devsuperior.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.tests.TokenUtil;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String adminToken, clientToken, invalidToken;
	private Long existUserId, nonExistUserId;	
	private User userClient, userAdmin;
	private UserDTO userClientDto, userAdminDto;
	
	@BeforeEach
	void setUp() throws Exception {
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		
		existUserId = 1L;
		nonExistUserId = 100L;
		
		userClient = UserFactory.createClientUser();
		userClientDto = new UserDTO(userClient);
		userAdmin = UserFactory.createAdminUser();
		userAdminDto = new UserDTO(userAdmin);
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		invalidToken = adminToken + "xpto";
	}
	
	@Test
	public void findMeShouldReturnDataWhenAdminLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/users/me")
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(2L));
		result.andExpect(jsonPath("$.name").value("Alex Green"));
		result.andExpect(jsonPath("$.email").value("alex@gmail.com"));
		result.andExpect(jsonPath("$.phone").value("977777777"));
		result.andExpect(jsonPath("$.birthDate").value("1987-12-13"));
		result.andExpect(jsonPath("$.roles").exists());
	}
	
	@Test
	public void findMeShouldReturnDataWhenClientLogged() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/users/me")
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(1L));
		result.andExpect(jsonPath("$.name").value("Maria Brown"));
		result.andExpect(jsonPath("$.email").value("maria@gmail.com"));
		result.andExpect(jsonPath("$.phone").value("988888888"));
		result.andExpect(jsonPath("$.birthDate").value("2001-07-25"));
		result.andExpect(jsonPath("$.roles").exists());
	}
	
	@Test
	public void findMeShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/users/me")
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
