package com.example.customerservice.controller;

import com.example.customerservice.dto.AccountDTO;
import com.example.customerservice.dto.CustomerDTO;
import com.example.customerservice.dto.DtoMapper;
import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.service.AccountService;
import com.example.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body(java.util.Collections.singletonMap("status", "OK"));
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return DtoMapper.toCustomerDTO(customerService.getCustomerById(id));
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers().stream().map(DtoMapper::toCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/{customerId}/account")
    public ResponseEntity<AccountDTO> getAccountByCustomerId(@PathVariable Long customerId) {
        Account account = accountService.getAccountByCustomerId(customerId);
        return ResponseEntity.ok(DtoMapper.toAccountDTO(account));
    }
}