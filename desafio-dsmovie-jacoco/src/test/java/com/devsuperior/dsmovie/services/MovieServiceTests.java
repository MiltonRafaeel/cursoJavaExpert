package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private Long existId, dependentId, nonExistId;
	
	private MovieEntity movie;
	private MovieDTO movieDto;
	
	PageImpl<MovieEntity> page;
	
	private String movieTitle;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		dependentId = 2L;
		nonExistId = 3L;
		
		movieTitle = "Invocacao do Mal";
		
		movie = MovieFactory.createMovieEntity();
		movieDto = MovieFactory.createMovieDTO();
		
		page = new PageImpl<>(List.of(movie));
		
		Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(any())).thenReturn(movie);
		
		Mockito.when(repository.getReferenceById(existId)).thenReturn(movie);
		Mockito.when(repository.getReferenceById(nonExistId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repository).deleteById(existId);
		Mockito.when(repository.existsById(existId)).thenReturn(true);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistId)).thenReturn(false);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		
		Pageable pageable = PageRequest.of(0, 12);
		String movieName = "Invocacao do Mal";
		
		Page<MovieDTO> result = service.findAll(movieName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getTitle(), movie.getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		
		MovieDTO result = service.findById(existId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		Assertions.assertEquals(result.getTitle(), movie.getTitle());
		Assertions.assertEquals(result.getImage(), movie.getImage());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		
		MovieDTO result = service.insert(movieDto);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), movie.getId());
		Assertions.assertEquals(result.getTitle(), movie.getTitle());
		Assertions.assertEquals(result.getImage(), movie.getImage());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		
		MovieDTO result = service.update(existId, movieDto);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		Assertions.assertEquals(result.getTitle(), movieDto.getTitle());
		Assertions.assertEquals(result.getScore(), movieDto.getScore());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistId, movieDto);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existId);
		});
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
