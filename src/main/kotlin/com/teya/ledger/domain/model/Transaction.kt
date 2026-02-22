package com.teya.ledger.domain.model

import com.teya.ledger.domain.exception.InsufficientFundsException
import com.teya.ledger.domain.exception.InvalidAmountException
import com.teya.ledger.domain.validation.Validatable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: UUID,
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: LocalDateTime,
    val updatedBalance: BigDecimal
) : Validatable {
    override fun validate() {
        validateAmount()
        validateUpdatedBalance()
    }

    private fun validateAmount() {
        if (amount < BigDecimal.ZERO) {
            throw InvalidAmountException(amount)
        }
    }

    private fun validateUpdatedBalance() {
        if (updatedBalance < BigDecimal.ZERO) {
            throw InsufficientFundsException(updatedBalance)
        }
    }
}
