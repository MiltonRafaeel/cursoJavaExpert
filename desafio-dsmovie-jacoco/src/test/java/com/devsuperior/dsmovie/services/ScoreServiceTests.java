package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ScoreRepository scoreRepository;
	
	@Mock 
	private MovieRepository movieRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CustomUserUtil userUtil;
	
	private String existMovie, nonExistMovie;
	private Long existId, nonExistId;
	private String username, nonExistUsername;
	
	private UserEntity user;
	private MovieEntity movie;
	private ScoreEntity score;
	
	private ScoreDTO scoreDto;
	private MovieDTO movieDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existMovie = "Clube da Luta";
		nonExistMovie = "filmeQualquer";
		existId = 1L;
		nonExistId = 2L;
		username = "maria@gmail.com";
		nonExistUsername = "naoexiste@gmail.com";
		
		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();
		score = ScoreFactory.createScoreEntity();
		
		scoreDto = ScoreFactory.createScoreDTO();
		movieDto = MovieFactory.createMovieDTO();
		
		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.findByUsername(nonExistUsername)).thenReturn(Optional.empty());
		
		Mockito.when(movieRepository.findById(existId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistId)).thenReturn(Optional.empty());
		Mockito.when(movieRepository.save(any())).thenReturn(movie); // ✅
		
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		
		MovieDTO result = service.saveScore(scoreDto);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(movie.getId(), result.getId());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
	}
}
