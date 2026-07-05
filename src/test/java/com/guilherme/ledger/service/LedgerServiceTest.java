package com.guilherme.ledger.service;

import com.guilherme.ledger.exception.InsufficientMoneyException;
import com.guilherme.ledger.model.domain.Transaction;
import com.guilherme.ledger.model.domain.TransactionType;
import com.guilherme.ledger.model.dto.request.AmountRequest;
import com.guilherme.ledger.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private LedgerService ledgerService;

    @Test
    void testDepositAddsToBalance() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        Transaction transaction = ledgerService.recordDeposit(new AmountRequest(new BigDecimal("100.00")));

        assertThat(transaction.type()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.amount()).isEqualByComparingTo("100.00");
        assertThat(transaction.balanceAfter()).isEqualByComparingTo("100.00");
        verify(transactionRepository).saveTransaction(transaction);
    }

    @Test
    void testWithdrawalSubtractsFromBalance() {
        when(transactionRepository.findAll()).thenReturn(List.of(deposit("100.00")));

        Transaction transaction = ledgerService.recordWithdrawal(new AmountRequest(new BigDecimal("30.00")));

        assertThat(transaction.type()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transaction.amount()).isEqualByComparingTo("30.00");
        assertThat(transaction.balanceAfter()).isEqualByComparingTo("70.00");
        verify(transactionRepository).saveTransaction(transaction);
    }

    @Test
    void testWithdrawalOfWholeBalanceLeavesZero() {
        when(transactionRepository.findAll()).thenReturn(List.of(deposit("50.00")));

        Transaction transaction = ledgerService.recordWithdrawal(new AmountRequest(new BigDecimal("50.00")));

        assertThat(transaction.balanceAfter()).isEqualByComparingTo("0.00");
        verify(transactionRepository).saveTransaction(transaction);
    }

    @Test
    void testWithdrawalAboveBalanceThrowsAndDoesNotSave() {
        when(transactionRepository.findAll()).thenReturn(List.of(deposit("50.00")));

        assertThatThrownBy(() -> ledgerService.recordWithdrawal(new AmountRequest(new BigDecimal("80.00"))))
                .isInstanceOf(InsufficientMoneyException.class);

        verify(transactionRepository, never()).saveTransaction(any());
    }

    @Test
    void testGetBalanceSumsDepositsAndWithdrawals() {
        when(transactionRepository.findAll())
                .thenReturn(List.of(deposit("100.00"), withdrawal("30.00"), deposit("10.00")));

        assertThat(ledgerService.getBalance()).isEqualByComparingTo("80.00");
    }

    @Test
    void testGetHistoryReturnsAllTransactions() {
        List<Transaction> transactions = List.of(deposit("100.00"), withdrawal("30.00"));
        when(transactionRepository.findAll()).thenReturn(transactions);

        assertThat(ledgerService.getHistory()).isEqualTo(transactions);
    }

    private Transaction deposit(String amount) {
        return Transaction.create(TransactionType.DEPOSIT, new BigDecimal(amount), BigDecimal.ZERO);
    }

    private Transaction withdrawal(String amount) {
        return Transaction.create(TransactionType.WITHDRAWAL, new BigDecimal(amount), BigDecimal.ZERO);
    }
}
