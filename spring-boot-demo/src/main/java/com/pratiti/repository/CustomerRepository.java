package com.pratiti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pratiti.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	public Optional<Customer> findByEmail(String email);
	
	public Optional<Customer> findById(Integer id);
	
	public boolean existsByEmail(String email);
	
	public boolean existsById(Integer id);
	

	public Optional<Customer> findByEmailAndPassword(String email , String password);
	
	
	

}