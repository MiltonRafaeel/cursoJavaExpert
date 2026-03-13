package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

//	@Query("SELECT obj FROM Movie obj WHERE "
//	         + "(:genreId = 0 OR obj.genre.id = :genreId) "
//	         + "ORDER BY obj.title")
//	Page<Movie> findAllPaged(Long genreId, Pageable pageable);
	
	@Query(nativeQuery = true, 
		       value = """
		               SELECT m.id, m.title, m.sub_title, m.movie_year, 
		                      m.img_url, m.synopsis,
		                      g.id AS genreId, g.name AS genreName
		               FROM tb_movie m
		               INNER JOIN tb_genre g ON g.id = m.genre_id
		               WHERE (:genreId = 0 OR m.genre_id = :genreId)
		               ORDER BY m.title
		               """,
		       countQuery = """
		               SELECT COUNT(*) FROM tb_movie m
		               WHERE (:genreId = 0 OR m.genre_id = :genreId)
		               """)
	Page<MovieCardProjection> searchAllPaged(Long genreId, Pageable pageable);
}
