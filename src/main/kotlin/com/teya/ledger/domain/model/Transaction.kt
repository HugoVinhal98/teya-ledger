package com.teya.ledger.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: UUID,
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: LocalDateTime,
    val balanceAfter: BigDecimal
)
