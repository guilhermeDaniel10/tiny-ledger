package com.guilherme.ledger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps domain errors to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Returns 422 when a withdrawal exceeds the balance.
     */
    @ExceptionHandler(InsufficientMoneyException.class)
    public ProblemDetail handleInsufficientMoney(InsufficientMoneyException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, exception.getMessage());
    }
}
