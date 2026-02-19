package com.teya.ledger.application.service

import com.teya.ledger.application.dto.*
import com.teya.ledger.domain.exception.InsufficientFundsException
import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class LedgerService(
    private val ledgerRepository: LedgerRepository
) {
    fun recordTransaction(request: RecordTransactionRequest): RecordTransactionResponse {
        val currentBalance = ledgerRepository.getBalance()

        // Validate business rules
        if (request.type == TransactionType.WITHDRAWAL && currentBalance < request.amount) {
            throw InsufficientFundsException(currentBalance = currentBalance, requestedAmount = request.amount)
        }

        // Calculate new balance based on transaction type
        val newBalance = when (request.type) {
            TransactionType.DEPOSIT -> currentBalance.add(request.amount)
            TransactionType.WITHDRAWAL -> currentBalance.subtract(request.amount)
        }

        // Create transaction domain object
        val transaction = Transaction(
            id = UUID.randomUUID(),
            type = request.type,
            amount = request.amount,
            timestamp = LocalDateTime.now(),
            balanceAfter = newBalance
        )

        // Save transaction and update balance
        ledgerRepository.save(transaction)

        return RecordTransactionResponse(
            transactionId = transaction.id,
            type = transaction.type,
            amount = transaction.amount,
            timestamp = transaction.timestamp,
            balanceAfter = transaction.balanceAfter
        )
    }

    fun getBalance(): BalanceResponse {
        val balance = ledgerRepository.getBalance()
        return BalanceResponse(
            balance = balance
        )
    }

    fun getTransactionHistory(): TransactionHistoryResponse {
        val transactions = ledgerRepository.getTransactionHistory()
        return TransactionHistoryResponse(
            transactions = transactions.map { transaction ->
                TransactionDto(
                    transactionId = transaction.id,
                    type = transaction.type,
                    amount = transaction.amount,
                    timestamp = transaction.timestamp,
                    balanceAfter = transaction.balanceAfter
                )
            }
        )
    }
}
