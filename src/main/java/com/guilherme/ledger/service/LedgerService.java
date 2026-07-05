package com.guilherme.ledger.service;

import com.guilherme.ledger.exception.InsufficientMoneyException;
import com.guilherme.ledger.model.domain.Transaction;
import com.guilherme.ledger.model.domain.TransactionType;
import com.guilherme.ledger.model.dto.request.AmountRequest;
import com.guilherme.ledger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Applies deposits and withdrawals and gets the balance from the stored transactions.
 */
@Service
public class LedgerService {
    private final TransactionRepository transactionRepository;

    public LedgerService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Records a deposit and returns the resulting transaction
     */
    public synchronized Transaction recordDeposit(AmountRequest amountRequest) {
        BigDecimal balanceBeforeTransaction = getBalance();
        BigDecimal amountToDeposit = amountRequest.amount();

        BigDecimal balanceAfterWithdrawal = balanceBeforeTransaction.add(amountToDeposit);

        Transaction transaction = Transaction.create(TransactionType.DEPOSIT, amountToDeposit, balanceAfterWithdrawal);
        transactionRepository.saveTransaction(transaction);

        return transaction;
    }

    /**
     * Records a withdrawal, or throws if the amount is larger than the current balance
     */
    public synchronized Transaction recordWithdrawal(AmountRequest amountRequest) {
        BigDecimal balanceBeforeTransaction = getBalance();
        BigDecimal amountToWithdrawal = amountRequest.amount();

        if (amountToWithdrawal.compareTo(balanceBeforeTransaction) > 0) {
            String withdrawalExceptionReason =
                    String.format("Amount to withdrawal %s exceeds balance %s.", amountToWithdrawal, balanceBeforeTransaction);
            throw new InsufficientMoneyException(withdrawalExceptionReason);
        }

        BigDecimal balanceAfterWithdrawal = balanceBeforeTransaction.subtract(amountToWithdrawal);

        Transaction transaction = Transaction.create(TransactionType.WITHDRAWAL, amountToWithdrawal, balanceAfterWithdrawal);
        transactionRepository.saveTransaction(transaction);

        return transaction;
    }

    /**
     * Computes the current balance from all recorded transactions
     */
    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactionRepository.findAll()) {
            balance = switch (transaction.type()) {
                case DEPOSIT -> balance.add(transaction.amount());
                case WITHDRAWAL -> balance.subtract(transaction.amount());
            };
        }
        return balance;
    }

    /**
     * Returns all transactions in insertion order
     */
    public List<Transaction> getHistory() {
        return transactionRepository.findAll();
    }
}
