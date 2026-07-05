package com.guilherme.ledger.exception;

/**
 * Thrown when a withdrawal is larger than the current balance.
 */
public class InsufficientMoneyException extends RuntimeException {
    public InsufficientMoneyException(String message) {
        super(message);
    }
}
