package com.guilherme.ledger.controller;

import com.guilherme.ledger.model.domain.Transaction;
import com.guilherme.ledger.model.dto.request.AmountRequest;
import com.guilherme.ledger.model.dto.response.BalanceResponse;
import com.guilherme.ledger.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoints for recording money movements and reading the balance and history.
 */
@RestController()
public class LedgerController {
    private static final String CURRENCY = "EUR";

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * Records a deposit.
     */
    @PostMapping("/deposits")
    public ResponseEntity<Transaction> record(@Valid @RequestBody AmountRequest request) {
        Transaction transaction = ledgerService.recordDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Records a withdrawal.
     */
    @PostMapping("/withdrawals")
    public ResponseEntity<Transaction> withdrawal(@Valid @RequestBody AmountRequest request) {
        Transaction transaction = ledgerService.recordWithdrawal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Returns the current balance.
     */
    @GetMapping("/balance")
    public BalanceResponse balance() {
        return new BalanceResponse(ledgerService.getBalance(), CURRENCY);
    }

    /**
     * Returns the full transaction history.
     */
    @GetMapping("/transactions")
    public List<Transaction> history() {
        return ledgerService.getHistory();
    }
}
