package com.guilherme.ledger.repository;

import com.guilherme.ledger.model.domain.Transaction;

import java.util.List;

public interface TransactionRepository {

    void addTransaction(Transaction transaction);

    List<Transaction> getAll();
}
