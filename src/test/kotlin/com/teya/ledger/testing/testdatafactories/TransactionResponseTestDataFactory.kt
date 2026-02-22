package com.teya.ledger.testing.testdatafactories

import com.teya.ledger.application.dto.TransactionResponse
import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object TransactionResponseTestDataFactory {

    fun create(
        transactionId: UUID = UUID.randomUUID(),
        type: TransactionType = TransactionType.entries.random(),
        amount: BigDecimal = BigDecimal.TWO,
        timestamp: LocalDateTime = LocalDateTime.now(),
        updatedBalance: BigDecimal = BigDecimal.TEN,
    ) = TransactionResponse(
        transactionId = transactionId,
        type = type,
        amount = amount,
        timestamp = timestamp,
        updatedBalance = updatedBalance,
    )
}
