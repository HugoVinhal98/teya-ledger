package com.teya.ledger.application.service

import com.teya.ledger.application.command.RecordTransactionCommand
import com.teya.ledger.application.dto.*
import com.teya.ledger.domain.factory.TransactionFactory
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Service

@Service
class LedgerService(
    private val ledgerRepository: LedgerRepository,
    private val transactionFactory: TransactionFactory
) {
    fun recordTransaction(command: RecordTransactionCommand): RecordTransactionResponse {
        val currentBalance = ledgerRepository.getBalance()

        // Create transaction domain object using factory (returns validated)
        val validatedTransaction = transactionFactory.create(
            type = command.type,
            amount = command.amount,
            currentBalance = currentBalance
        )

        // Save transaction
        ledgerRepository.save(validatedTransaction)

        val transaction = validatedTransaction.unwrap()
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
