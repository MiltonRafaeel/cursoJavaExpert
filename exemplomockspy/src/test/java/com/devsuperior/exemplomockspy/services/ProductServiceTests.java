package com.devsuperior.exemplomockspy.services;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.devsuperior.exemplomockspy.dto.ProductDTO;
import com.devsuperior.exemplomockspy.entities.Product;
import com.devsuperior.exemplomockspy.repositories.ProductRepository;
import com.devsuperior.exemplomockspy.services.exceptions.InvalidDataException;

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
		MockitoAnnotations.openMocks(this);

		product = new Product(1L, "Playstation", 2000.0);
		productDto = new ProductDTO(product);

		existId = 1L;
		nonExistId = 2L;

		Mockito.when(repository.save(any())).thenReturn(product);

		Mockito.when(repository.getReferenceById(existId)).thenReturn(product);

		Mockito.when(repository.getReferenceById(nonExistId)).thenThrow(EntityNotFoundException.class);
	}

	@Test
	public void insertShouldReturnProductDTOWhenValidDate() {

		ProductService productSpy = Mockito.spy(service);
		ReflectionTestUtils.setField(productSpy, "repository", repository);

		Mockito.doNothing().when(productSpy).validateData(productDto);

		ProductDTO result = productSpy.insert(productDto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getName(), "Playstation");
	}

	@Test
	public void insertShouldReturnInvalidDataExceptionWhenProductNameIsBlank() {

		productDto.setName("");

		ProductService productSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(productSpy).validateData(productDto);

		Assertions.assertThrows(InvalidDataException.class, () -> {
			@SuppressWarnings("unused")
			ProductDTO result = productSpy.insert(productDto);
		});
	}

	@Test
	public void insertShouldReturnInvalidDataExceptionWhenProductPriceIsNegativeOrZero() {

		productDto.setPrice(-5.0);

		ProductService productSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(productSpy).validateData(productDto);

		Assertions.assertThrows(InvalidDataException.class, () -> {
			@SuppressWarnings("unused")
			ProductDTO result = productSpy.insert(productDto);
		});
	}
}
