package com.guilherme.ledger.repository;

import com.guilherme.ledger.model.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory transaction store with CopyOnWriteArrayList.
 */
@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactions);
    }
}
