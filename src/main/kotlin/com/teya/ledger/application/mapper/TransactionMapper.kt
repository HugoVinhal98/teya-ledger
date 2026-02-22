package com.teya.ledger.application.mapper

import com.teya.ledger.application.dto.TransactionResponse
import com.teya.ledger.domain.model.Transaction
import org.springframework.stereotype.Component

@Component
class TransactionMapper {
    fun toResponse(transaction: Transaction): TransactionResponse {
        return TransactionResponse(
            transactionId = transaction.id,
            type = transaction.type,
            amount = transaction.amount,
            timestamp = transaction.timestamp,
            updatedBalance = transaction.updatedBalance
        )
    }
}
