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
    ): Validated<Transaction> {
        val transaction = Transaction(
            id = UUID.randomUUID(),
            type = type,
            amount = amount,
            timestamp = LocalDateTime.now(),
        )
        return Validated(transaction)
    }
}
