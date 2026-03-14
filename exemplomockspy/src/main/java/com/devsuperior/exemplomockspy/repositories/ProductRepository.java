package com.devsuperior.exemplomockspy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.exemplomockspy.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
