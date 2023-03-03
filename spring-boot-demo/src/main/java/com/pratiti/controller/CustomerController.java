package com.pratiti.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pratiti.entity.Address;
import com.pratiti.entity.Customer;
import com.pratiti.exception.CustomerServiceException;
import com.pratiti.model.ActivationStatus;
import com.pratiti.model.CustomerData;
import com.pratiti.model.LoginData;
import com.pratiti.model.LoginStatus;
import com.pratiti.model.RegistrationStatus;
import com.pratiti.service.CustomerService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@RestController
@CrossOrigin
public class CustomerController {
	@Autowired
	CustomerService customerService;


	@PostMapping(path="/register")
	public RegistrationStatus register(CustomerData customerData) {
		//System.out.println(customerData.getProfilePic().getOriginalFilename());
		try {
			
			//copying the uploded image to the folder
			String uploadDir = "C:\\Users\\unmes\\OneDrive\\Desktop\\Pratiti traning\\Profile Pic\\";
			InputStream f1 = customerData.getProfilePic().getInputStream();
			String finalPath = uploadDir + customerData.getProfilePic().getOriginalFilename();
			System.out.println(finalPath);
			FileOutputStream f2 = new FileOutputStream(finalPath);
			FileCopyUtils.copy(f1, f2);
			
			Customer customer = new Customer();
			BeanUtils.copyProperties(customerData, customer);
			customer.setProfilePic(customerData.getProfilePic().getOriginalFilename());
			int id = customerService.register(customer);
			RegistrationStatus registrationStatus = new RegistrationStatus();
			registrationStatus.setStatus(true);
			registrationStatus.setMessage("Customer register successfully");
			registrationStatus.setRegisteredCustomerId(id);
			return registrationStatus;
		} catch (IOException | CustomerServiceException e) {
			RegistrationStatus registrationStatus = new RegistrationStatus();
			registrationStatus.setStatus(false);
			registrationStatus.setMessage(e.getMessage());
			return registrationStatus;
		}
	}
	
	
//	//RegistrationStatus is used to return the message in form of json
//		@PostMapping("/register")
//		public RegistrationStatus register(@RequestBody Customer customer) {
//			try {
//				int id = customerService.register(customer);
//				RegistrationStatus registrationStatus = new RegistrationStatus();
//				registrationStatus.setStatus(true);
//				registrationStatus.setMessage("Customer register successfully");
//				registrationStatus.setRegisteredCustomerId(id);
//				return registrationStatus;
////				return "Customer register successfully";
//			} catch (CustomerServiceException e) {
//				RegistrationStatus registrationStatus = new RegistrationStatus();
//				registrationStatus.setStatus(false);
//				registrationStatus.setMessage(e.getMessage());
//				return registrationStatus;
////				return e.getMessage();
//			}
//		}

	@GetMapping("/active-account")
	public ActivationStatus activate(@RequestParam("id") int id) {
		try {
			customerService.activate(id);
			ActivationStatus activationStatus = new ActivationStatus();
			activationStatus.setStatus(true);
			activationStatus.setMessage("Customer status updated successfully");
			activationStatus.setRegisteredCustomerId(id);
			return activationStatus;
		} catch (CustomerServiceException e) {
			ActivationStatus activationStatus = new ActivationStatus();
			activationStatus.setStatus(false);
			activationStatus.setMessage(e.getMessage());
			return activationStatus;
		}
	}

	@PostMapping("/login")
	public LoginStatus login(@RequestBody LoginData loginData) {
		try {
			Customer customer = customerService.login(loginData.getEmail(), loginData.getPassword());
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(true);
			loginStatus.setId(customer.getId());
			loginStatus.setName(customer.getName());
			loginStatus.setMessage("Login successfully");
			return loginStatus;
		} catch (CustomerServiceException e) {
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(false);
			loginStatus.setMessage(e.getMessage());
			return loginStatus;
		}
	}
	
	@GetMapping("/view-details")
	public Customer viewDetails(@RequestParam("id") int id) {
		return customerService.ViewDetails(id);
	}

//	@GetMapping("/view-profile")
//	public Customer viewProfile(@RequestParam("id") int id) {
//		Customer customer = customerService.viewProfile(id);
//		return customer;
//	}

}
