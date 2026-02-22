package com.teya.ledger.domain.exception

import java.math.BigDecimal

class InvalidAmountException(
    requestedAmount: BigDecimal
) : RuntimeException("Invalid amount: the amount of $requestedAmount is not valid. Amount must be a non negative value.")
