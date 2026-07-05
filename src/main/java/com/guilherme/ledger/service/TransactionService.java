package com.guilherme.ledger.service;

import com.guilherme.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void recordDeposit(String amount) {

    }

    public void recordWithdrawal(String amount) {

    }
}
