package com.teya.ledger.domain.factory

import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.validation.Validated
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Component
class TransactionFactory {
    fun create(
        type: TransactionType,
        amount: BigDecimal,
        currentBalance: BigDecimal
    ): Validated<Transaction> {
        val updatedBalance = when (type) {
            TransactionType.DEPOSIT -> currentBalance + amount
            TransactionType.WITHDRAWAL -> currentBalance - amount
        }

        val transaction = Transaction(
            id = UUID.randomUUID(),
            type = type,
            amount = amount,
            timestamp = LocalDateTime.now(),
            updatedBalance = updatedBalance
        )
        return Validated(transaction)
    }
}
