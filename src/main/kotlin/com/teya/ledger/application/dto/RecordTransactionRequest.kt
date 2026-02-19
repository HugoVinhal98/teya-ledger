package com.teya.ledger.application.dto

import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal

data class RecordTransactionRequest(
    val type: TransactionType,
    val amount: BigDecimal
)
