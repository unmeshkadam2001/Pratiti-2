package com.pratiti.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratiti.entity.Address;
import com.pratiti.entity.Customer;
import com.pratiti.entity.Customer.Status;
import com.pratiti.exception.CustomerServiceException;
import com.pratiti.model.LoginData;
import com.pratiti.repository.AddressRepository;
import com.pratiti.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	                                                                                           
	@Autowired
	private AddressRepository addressRepository;
	
	public int register(Customer customer) {
		if(customerRepository.existsByEmail(customer.getEmail()) == false) {
			customer.setStatus(Status.INACTIVE);
			customer.getAddress().setCustomer(customer);
			customerRepository.save(customer);
			//code to send email --> emailService.sendEmailRegistration(customer.getEmail());
			return customer.getId();
		}
		else {
			throw new CustomerServiceException("Customer with email " + customer.getEmail() + " already exists.");
		}
	}
	
	public void activate(int id) {
		if(customerRepository.existsById(id) == true) {
			Optional<Customer> c = customerRepository.findById(id);
			Customer custData = c.get();
			if(custData.getStatus()==Status.ACTIVE) {
				throw new CustomerServiceException("Account is already activate");
			}
			else if(custData.getStatus()!=Status.LOCKED) {
				custData.setStatus(Status.ACTIVE);
				customerRepository.save(custData);
				
			}
			else
				throw new CustomerServiceException("Account is LOCKED");
		}
		else {
			throw new CustomerServiceException("Customer not exists.");
		}
	}
	
	
	public Customer login(String email , String password) {
		Optional<Customer> c = customerRepository.findByEmailAndPassword(email,password);
		if(c.isPresent()) {
			
			Customer custData = c.get();
			if(custData.getStatus()==Status.LOCKED) {
				throw new CustomerServiceException("Account is LOCKED");
			}
			else if(custData.getStatus()==Status.ACTIVE) {
				return custData;
			}
			else 
				throw new CustomerServiceException("Account is INACTIVE");
		}
		else 
		{
			System.out.println("inside");
			throw new CustomerServiceException("Enter valid email address or password");
		}
	}
	
	public Customer ViewDetails(int id) {		
		return customerRepository.findById(id).get();		
	}
	

	
//	public int insertAddress(Address address,int idOfCustomer) {
//		if(customerRepository.findById(idOfCustomer) != null) {
//			address.getCustomer().setAddress(address);;
//			addressRepository.save(address);
//			//code to send email --> emailService.sendEmailRegistration(customer.getEmail());
//			return address.getId();
//		}
//		else {
//			throw new CustomerServiceException("Customer not exists.");
//		}
//	}
	
	
}
