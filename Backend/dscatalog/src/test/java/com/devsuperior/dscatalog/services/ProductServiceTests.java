package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existId;
	private long nonExistId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		nonExistId = 1000L;
		dependentId = 5L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		//quando -> argumento -> acao(quando acontecer algo, passando o argumento, faca isso(retorne page))
		Mockito.when(repository.findAll( (Pageable) ArgumentMatchers.any())).thenReturn(page);
		
		//aqui simulei duas situacoes, quando encontro o objeto por id e quando nao encontro.
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistId)).thenReturn(Optional.empty());
		
		//quando -> argumento -> acao(quando acontecer algo, passando o argumento, faca isso(retorne product))
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		//quando -> acao(quando acontecer algo, faca isso)
		Mockito.when(repository.existsById(existId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);

		//acao -> quando(digo a acao quando ocorrer algo, isso quando o retorno e void, como no caso do delete)
		Mockito.doNothing().when(repository).deleteById(existId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnObjectWhenExistId() {
		
		ProductDTO result = service.findById(existId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findById(existId);
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenDoesNotExistId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistId);
		});
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
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
}
