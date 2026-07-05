package com.guilherme.ledger.repository;

import com.guilherme.ledger.model.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);

    }

    @Override
    public List<Transaction> getAll() {
        return List.copyOf(transactions);
    }
}
