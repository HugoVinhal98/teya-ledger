package com.teya.ledger.application.dto

import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionHistoryResponse(
    val transactions: List<TransactionDto>
)

data class TransactionDto(
    val transactionId: UUID,
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: LocalDateTime,
    val balanceAfter: BigDecimal
)
