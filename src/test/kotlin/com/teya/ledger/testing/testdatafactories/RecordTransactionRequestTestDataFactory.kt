package com.teya.ledger.testing.testdatafactories

import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.presentation.dto.RecordTransactionRequest
import java.math.BigDecimal

object RecordTransactionRequestTestDataFactory {

    fun create(
        type: TransactionType = TransactionType.entries.random(),
        amount: BigDecimal = BigDecimal("100.00")
    ) = RecordTransactionRequest(
        type = type,
        amount = amount
    )
}
