package com.teya.ledger.domain.exception

import java.math.BigDecimal

class InsufficientFundsException(
    val currentBalance: BigDecimal,
    val requestedAmount: BigDecimal
) : RuntimeException("Insufficient funds: current balance is $currentBalance, but requested withdrawal of $requestedAmount")
