package com.guilherme.ledger.model.dto.response;

import java.math.BigDecimal;

public record BalanceResponse(BigDecimal balance, String currency) {
}
