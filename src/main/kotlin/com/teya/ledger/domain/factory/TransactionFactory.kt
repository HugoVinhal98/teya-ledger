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
        val balanceAfter = when (type) {
            TransactionType.DEPOSIT -> currentBalance.add(amount)
            TransactionType.WITHDRAWAL -> currentBalance.subtract(amount)
        }

        val transaction = Transaction(
            id = UUID.randomUUID(),
            type = type,
            amount = amount,
            timestamp = LocalDateTime.now(),
            balanceAfter = balanceAfter
        )
        return Validated(transaction)
    }
}
