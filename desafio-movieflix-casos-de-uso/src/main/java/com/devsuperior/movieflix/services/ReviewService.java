package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private AuthService service;

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		
		User user = service.authenticated();
				
		Optional<Movie> movie = movieRepository.findById(dto.getMovieId());
		Movie result = movie.get();
		
		try {
			Review entity = new Review();
			
			entity.setText(dto.getText());
			entity.setMovie(result);
			entity.setUser(user);
			
			entity = reviewRepository.save(entity);
			return new ReviewDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("nao encontrado");
		}
	}
}
