package com.devsuperior.exemplomockspy.services;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.exemplomockspy.dto.ProductDTO;
import com.devsuperior.exemplomockspy.entities.Product;
import com.devsuperior.exemplomockspy.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private Product product;
	private ProductDTO productDto;
	private Long existId, nonExistId;
	
	@BeforeEach
	void setUp() throws Exception {
		
		product = new Product(1L, "Playstation", 2000.0);
		productDto = new ProductDTO(product);
		
		existId = 1L;
		nonExistId = 2L;
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(existId)).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(nonExistId)).thenThrow(EntityNotFoundException.class);
	}
}
