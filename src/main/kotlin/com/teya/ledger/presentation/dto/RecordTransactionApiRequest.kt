package com.teya.ledger.presentation.dto

import com.teya.ledger.domain.model.TransactionType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class RecordTransactionApiRequest(
    @field:NotNull(message = "Transaction type is required")
    val type: TransactionType,

    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    val amount: BigDecimal
)
