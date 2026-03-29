package com.devsuperior.dscommerce.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dscommerce.tests.TokenUtil;

import io.restassured.http.ContentType;

public class OrderControllerRA {
	
	private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
	private String clientToken, adminToken, invalidToken, adminOnlyToken;
	private Long exstingOrderId, nonExistingOrderId;
	
	Map<String, Object> postOrderInstance;
	
	@BeforeEach
	public void setUp() {
		baseURI = "http://localhost:8080";
		
		exstingOrderId = 1L;
		nonExistingOrderId = 100L;
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		adminOnlyUsername = "milton@gmail.com";
		adminOnlyPassword = "123456";
		
		clientToken = TokenUtil.obtainAccesToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccesToken(adminUsername, adminPassword);
		adminOnlyToken = TokenUtil.obtainAccesToken(adminOnlyUsername, adminOnlyPassword);
		invalidToken = adminToken + "xpto";
		
		postOrderInstance = new HashMap<>();
		List<Map<String, Object>> items = new ArrayList<>();
		
		Map<String, Object> list0 = new HashMap<>();
		list0.put("productId", 1);
		list0.put("quantity", 2);
		
		items.add(list0);
		
		Map<String, Object> list1 = new HashMap<>();
		list1.put("productId", 5);
		list1.put("quantity", 1);
		
		items.add(list1);
		
		postOrderInstance.put("items", items);
	}
	
	@Test
	public void findByIdShouldReturnOrderWhenIdExistsAndAdminLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", exstingOrderId)
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("moment", equalTo("2022-07-25T13:00:00Z"))
			.body("status", equalTo("PAID"))
			.body("client.name", equalTo("Maria Brown"))
			.body("payment.moment", equalTo("2022-07-25T15:00:00Z"))
			.body("items.name[0]", equalTo("The Lord of the Rings"))
			.body("items.name[1]", equalTo("Macbook Pro"))
			.body("total", is(1431.0F));
	}
	
	@Test
	public void findByIdShouldReturnOrderWhenIdExistsAndClientLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", exstingOrderId)
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("moment", equalTo("2022-07-25T13:00:00Z"))
			.body("status", equalTo("PAID"))
			.body("client.name", equalTo("Maria Brown"))
			.body("payment.moment", equalTo("2022-07-25T15:00:00Z"))
			.body("items.name[0]", equalTo("The Lord of the Rings"))
			.body("items.name[1]", equalTo("Macbook Pro"))
			.body("total", is(1431.0F));
	}
	
	@Test
	public void findByIdShouldReturnForbbidenWhenClientLoggedAndOrderDoesNotBelongUser() {
		Long otherOrder = 2L;
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", otherOrder)
		.then()
			.statusCode(403);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", nonExistingOrderId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistsAndClientLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", nonExistingOrderId)
		.then()
			.statusCode(404);
	}
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenInvalidToken() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.accept(ContentType.JSON)
		.when()
			.get("/orders/{id}", exstingOrderId)
		.then()
			.statusCode(401);
	}
	
	@Test
	public void insertShouldReturnOrderWhenClientLogged() {
		JSONObject newOrder = new JSONObject(postOrderInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newOrder)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/orders")
		.then()
			.statusCode(201)
			.body("id", is(4));
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenInvalidItemsAndClientLogged() {
		postOrderInstance.put("items", null);
		JSONObject newOrder = new JSONObject(postOrderInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newOrder)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/orders")
		.then()
			.statusCode(422)
			.body("errors.message[0]", equalTo("Deve ter pelo menos um item"));
	}
	
	@Test
	public void insertShouldReturnForbbidenWhenAdminLogged() {
		JSONObject newOrder = new JSONObject(postOrderInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminOnlyToken)
			.body(newOrder)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/orders")
		.then()
			.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() {
		JSONObject newOrder = new JSONObject(postOrderInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newOrder)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/orders")
		.then()
			.statusCode(401);
	}
}
