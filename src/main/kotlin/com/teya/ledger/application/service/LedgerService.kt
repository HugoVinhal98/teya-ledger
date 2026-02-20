package com.teya.ledger.application.service

import com.teya.ledger.application.command.RecordTransactionCommand
import com.teya.ledger.application.dto.*
import com.teya.ledger.domain.factory.TransactionFactory
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Service

@Service
class LedgerService(
    private val ledgerRepository: LedgerRepository,
    private val transactionFactory: TransactionFactory
) {
    fun recordTransaction(command: RecordTransactionCommand): TransactionResponse {
        val currentBalance = ledgerRepository.getBalance()

        // Create transaction domain object using factory (returns validated)
        val validatedTransaction = transactionFactory.create(
            type = command.type,
            amount = command.amount,
        )

        val updatedBalance = when (command.type) {
            TransactionType.DEPOSIT -> currentBalance.add(command.amount)
            TransactionType.WITHDRAWAL -> currentBalance.subtract(command.amount)
        }

        // Save transaction and update balance
        ledgerRepository.save(validatedTransaction)
        ledgerRepository.updateBalance(updatedBalance)

        val transaction = validatedTransaction.unwrap()
        return TransactionResponse(
            transactionId = transaction.id,
            type = transaction.type,
            amount = transaction.amount,
            timestamp = transaction.timestamp,
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
                TransactionResponse(
                    transactionId = transaction.id,
                    type = transaction.type,
                    amount = transaction.amount,
                    timestamp = transaction.timestamp,
                )
            }
        )
    }
}
