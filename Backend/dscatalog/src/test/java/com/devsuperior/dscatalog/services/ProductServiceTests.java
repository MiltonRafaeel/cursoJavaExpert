package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existId;
	private long nonExistId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 1000L;
		dependentId = 5L;
		
		Mockito.when(repository.existsById(existId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);

		Mockito.doNothing().when(repository).deleteById(existId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistId);
		});
	}
}
