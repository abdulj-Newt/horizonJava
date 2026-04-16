package com.example.customerservice.controller;

import com.example.customerservice.dto.DtoMapper;
import com.example.customerservice.dto.TransactionDTO;
import com.example.customerservice.model.Transaction;
import com.example.customerservice.model.TransactionRequest;
import com.example.customerservice.exception.InvalidAmountException;
import com.example.customerservice.model.BillPayRequest;
import com.example.customerservice.model.AddMoneyRequest;
import com.example.customerservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Enforce > 0 for all endpoints
    private void validateAmount(java.math.BigDecimal amount) {
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Enter a Valid Amount");
        }
    }

    @PostMapping
    public TransactionDTO sendMoney(@RequestBody TransactionRequest transactionRequest) {
        validateAmount(transactionRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.transferMoneyByPLSQL(
                transactionRequest.getFromCustomerId(),
                transactionRequest.getToCustomerId(),
                transactionRequest.getAmount()
            )
        );
    }

    @PostMapping("/plsql")
    public TransactionDTO sendMoneyByPLSQL(@RequestBody TransactionRequest transactionRequest) {
        validateAmount(transactionRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.transferMoneyByPLSQL(
                transactionRequest.getFromCustomerId(),
                transactionRequest.getToCustomerId(),
                transactionRequest.getAmount()
            )
        );
    }

    @PostMapping("/bill-pay")
    public TransactionDTO payBill(@RequestBody BillPayRequest billPayRequest) {
        validateAmount(billPayRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.payBillByPLSQL(
                billPayRequest.getCustomerId(),
                billPayRequest.getBillType(),
                billPayRequest.getAccountNumber(),
                billPayRequest.getAmount()
            )
        );
    }

    @PostMapping("/bill-pay/plsql")
    public TransactionDTO payBillByPLSQL(@RequestBody BillPayRequest billPayRequest) {
        validateAmount(billPayRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.payBillByPLSQL(
                billPayRequest.getCustomerId(),
                billPayRequest.getBillType(),
                billPayRequest.getAccountNumber(),
                billPayRequest.getAmount()
            )
        );
    }

    @PostMapping("/add-money")
    public TransactionDTO addMoney(@RequestBody AddMoneyRequest addMoneyRequest) {
        validateAmount(addMoneyRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.addMoneyByPLSQL(
                addMoneyRequest.getCustomerId(),
                addMoneyRequest.getAmount()
            )
        );
    }

    @PostMapping("/add-money/plsql")
    public TransactionDTO addMoneyByPLSQL(@RequestBody AddMoneyRequest addMoneyRequest) {
        validateAmount(addMoneyRequest.getAmount());
        return DtoMapper.toTransactionDTO(
            transactionService.addMoneyByPLSQL(
                addMoneyRequest.getCustomerId(),
                addMoneyRequest.getAmount()
            )
        );
    }

    @GetMapping("/customer/{customerId}")
    public List<TransactionDTO> getTransactions(@PathVariable Long customerId) {
        return transactionService.getTransactionsByCustomerId(customerId)
                                 .stream()
                                 .map(DtoMapper::toTransactionDTO)
                                 .collect(Collectors.toList());
    }
}
