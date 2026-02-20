package com.devsuperior.dscatalog.repositories;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existId;
	private long nonExistId;
	private long countTotalProduct;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 50L;
		countTotalProduct = 25L;
	}
	
	@Test
	public void deleteShouldObjectWhenIdExist() {
		//A
		repository.deleteById(existId);
		//A
		Optional<Product> result = repository.findById(existId);
		//A
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals( countTotalProduct + 1, product.getId() );
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists() {
		
		Optional<Product> result = repository.findById(existId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExists() {
		
		Optional<Product> result = repository.findById(nonExistId);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
