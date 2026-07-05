package com.guilherme.ledger.repository;

import com.guilherme.ledger.model.domain.Transaction;

import java.util.List;

/**
 * Stores transactions and reads them back.
 */
public interface TransactionRepository {

    /**
     * Adds transaction to the store.
     */
    void saveTransaction(Transaction transaction);

    /**
     * Returns all stored transactions.
     */
    List<Transaction> findAll();
}
