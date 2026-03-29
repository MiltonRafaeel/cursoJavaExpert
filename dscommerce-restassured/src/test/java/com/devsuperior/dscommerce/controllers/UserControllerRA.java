package com.devsuperior.dscommerce.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dscommerce.tests.TokenUtil;

import io.restassured.http.ContentType;

public class UserControllerRA {
	
	private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
	private String clientToken, adminToken, invalidToken, adminOnlyToken;	
	
	@BeforeEach
	public void setUp() {
		baseURI = "http://localhost:8080";
		
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
	}
	
	@Test
	public void findMeShouldReturnUserAdminWhenAdminLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.accept(ContentType.JSON)
		.when()
			.get("/users/me")
		.then()
			.statusCode(200)
			.body("id", is(2));
	}
	
	@Test
	public void findMeShouldReturnUserClientWhenClientLogged() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.accept(ContentType.JSON)
		.when()
			.get("/users/me")
		.then()
			.statusCode(200)
			.body("id", is(1));
	}
	
	@Test
	public void findMeShouldReturnUnauthorizedWhenInvalidToken() {
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.accept(ContentType.JSON)
		.when()
			.get("/users/me")
		.then()
			.statusCode(401);
	}
}
