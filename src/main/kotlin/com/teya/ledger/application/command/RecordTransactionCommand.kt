package com.teya.ledger.application.command

import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal

data class RecordTransactionCommand(
    val type: TransactionType,
    val amount: BigDecimal
)
