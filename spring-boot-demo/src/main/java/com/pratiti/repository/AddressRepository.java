package com.pratiti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pratiti.entity.Address;
import com.pratiti.entity.Customer;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	public Optional<Address> findByCustomerId(Integer id);
}
