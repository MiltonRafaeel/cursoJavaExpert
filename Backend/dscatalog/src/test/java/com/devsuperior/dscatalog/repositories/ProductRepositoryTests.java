package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existId;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
	}
	
	@Test
	public void deleteShouldObjectWhenIdExist() {
		//A
		repository.deleteById(existId);
		//A
		Optional<Product> result = repository.findById(existId);
		Assertions.assertFalse(result.isPresent());
	}
}
