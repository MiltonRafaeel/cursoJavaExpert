package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.demo.dto.EmployeeDTO;
import com.devsuperior.demo.entities.Employee;
import com.devsuperior.demo.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository repository;
	
	public Page<EmployeeDTO> findAll(Pageable pageable) {
		Page<Employee> result = repository.findAll(pageable);
		return result.map(x -> new EmployeeDTO(x));
	}

}
