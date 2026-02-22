package com.teya.ledger.domain.exception

import java.math.BigDecimal

class InsufficientFundsException(
    requestedAmount: BigDecimal
) : RuntimeException("Insufficient funds: you don't have the funds for the requested withdrawal of $requestedAmount.")
