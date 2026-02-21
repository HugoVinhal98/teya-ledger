package com.teya.ledger.testing.testdatafactories

import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.validation.Validated
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object TransactionTestDataFactory {

    fun create(
        id: UUID = UUID.randomUUID(),
        type: TransactionType = TransactionType.entries.random(),
        amount: BigDecimal = BigDecimal.TWO,
        timestamp: LocalDateTime = LocalDateTime.now(),
    ) = Transaction(
        id = id,
        type = type,
        amount = amount,
        timestamp = timestamp,
    )

    fun createValid(
        id: UUID = UUID.randomUUID(),
        type: TransactionType = TransactionType.entries.random(),
        amount: BigDecimal = BigDecimal.TWO,
        timestamp: LocalDateTime = LocalDateTime.now(),
    ) = Validated(create(
        id = id,
        type = type,
        amount = amount,
        timestamp = timestamp,
    ))

    fun createValidDeposit(
        id: UUID = UUID.randomUUID(),
        amount: BigDecimal = BigDecimal.TWO,
        timestamp: LocalDateTime = LocalDateTime.now(),
    ) = createValid(
        id = id,
        type = TransactionType.DEPOSIT,
        amount = amount,
        timestamp = timestamp,
    )
}