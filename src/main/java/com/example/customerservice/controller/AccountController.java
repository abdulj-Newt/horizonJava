package com.example.customerservice.controller;

import com.example.customerservice.dto.AccountDTO;
import com.example.customerservice.dto.DtoMapper;
import com.example.customerservice.model.Account;
import com.example.customerservice.model.AddMoneyRequest;
import com.example.customerservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    // Legacy: Using Vector instead of List
    private Vector<String> auditLog = new Vector<String>();
    
    // Legacy: Using Hashtable instead of HashMap
    private Hashtable<String, Object> cache = new Hashtable<String, Object>();

    @GetMapping
    public AccountDTO getAccountByUsername(@RequestParam String username, HttpServletRequest request) {
        // Legacy: Manual null check instead of validation annotations
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        auditLog.add("Getting account for username: " + username);
        return DtoMapper.toAccountDTO(accountService.getAccountByUsername(username));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity getAccountByCustomerId(@PathVariable Long customerId, HttpServletResponse response) {
        // Legacy: Raw ResponseEntity without generics
        try {
            if (customerId == null || customerId <= 0) {
                return new ResponseEntity("Invalid customer ID", HttpStatus.BAD_REQUEST);
            }
            AccountDTO account = DtoMapper.toAccountDTO(accountService.getAccountByCustomerId(customerId));
            return new ResponseEntity(account, HttpStatus.OK);
        } catch (Exception e) {
            // Legacy: Catching generic Exception
            return new ResponseEntity("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable long accountId) {
        // Legacy: No validation, direct service call
        return accountService.getBalance(accountId);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity deposit(@PathVariable long accountId, @RequestBody Map request) {
        // Legacy: Raw Map without generics
        try {
            Object amountObj = request.get("amount");
            if (amountObj == null) {
                return new ResponseEntity("Amount is required", HttpStatus.BAD_REQUEST);
            }
            
            BigDecimal amount;
            if (amountObj instanceof String) {
                amount = new BigDecimal((String) amountObj);
            } else if (amountObj instanceof Number) {
                amount = new BigDecimal(amountObj.toString());
            } else {
                return new ResponseEntity("Invalid amount format", HttpStatus.BAD_REQUEST);
            }
            
            BigDecimal result = accountService.deposit(accountId, amount);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Deposit failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity withdraw(@PathVariable long accountId, @RequestBody Map request) {
        // Legacy: Raw Map, manual exception handling
        BigDecimal amount = null;
        try {
            amount = new BigDecimal(request.get("amount").toString());
        } catch (Exception e) {
            return new ResponseEntity("Invalid amount", HttpStatus.BAD_REQUEST);
        }
        
        try {
            BigDecimal result = accountService.withdraw(accountId, amount);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-money")
    public ResponseEntity<String> addMoney(@RequestBody AddMoneyRequest request) {
        // Legacy: Manual validation instead of @Valid
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is required");
        }
        if (request.getCustomerId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer ID is required");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be positive");
        }
        
        try {
            accountService.addMoney(request.getCustomerId(), request.getAmount());
            return ResponseEntity.ok("Money added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add money: " + e.getMessage());
        }
    }
    
    // Legacy: Deprecated method
    @Deprecated
    @GetMapping("/legacy-balance/{accountId}")
    public String getLegacyBalance(@PathVariable long accountId) {
        // Legacy: String concatenation instead of proper response format
        BigDecimal balance = accountService.getBalance(accountId);
        return "Account " + accountId + " has balance: $" + balance.toString();
    }
}