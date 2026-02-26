package com.devsuperior.bds02.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.exceptions.DatabaseException;
import com.devsuperior.bds02.exceptions.ResourceNotFoundException;
import com.devsuperior.bds02.repositories.CityRepository;

@Service
public class CityService {
	
	@Autowired
	private CityRepository repository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> result = repository.findAll(Sort.by("name"));
		return result.stream().map(x -> new CityDTO(x)).toList();
	}

	public CityDTO insert(CityDTO dto) {
		
		City entity = new City();
		entity.setName(dto.getName());
		
		entity = repository.save(entity);
		return new CityDTO(entity);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("id nao encontrado");
		}
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("erro: id relacionado a alguem");
		}
		
	}

}
