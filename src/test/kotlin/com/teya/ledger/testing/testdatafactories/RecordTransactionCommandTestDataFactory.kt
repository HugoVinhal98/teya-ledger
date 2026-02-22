package com.teya.ledger.testing.testdatafactories

import com.teya.ledger.application.command.RecordTransactionCommand
import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal

object RecordTransactionCommandTestDataFactory {

    fun create(
        type: TransactionType = TransactionType.entries.random(),
        amount: BigDecimal = BigDecimal("100.00")
    ) = RecordTransactionCommand(
        type = type,
        amount = amount
    )
}
