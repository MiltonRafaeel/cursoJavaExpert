package com.devsuperior.dscatalog.services;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceIT {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ProductService service;
	
	private Long existId;
	private Long nonExistId;
	private Long countTotalProduct;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 1000L;
		countTotalProduct = 25L;
		
		/*nao precisei da simulacao para o dele, pois por ser um teste de integracao, ele ja vai ao H2.
		 *nao precisei simular.
		 */
		
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existId);
		
		Assertions.assertEquals(( countTotalProduct - 1 ), repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistId);
		});		
	}
}
