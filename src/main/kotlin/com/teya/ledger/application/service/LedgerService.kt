package com.teya.ledger.application.service

import com.teya.ledger.application.command.RecordTransactionCommand
import com.teya.ledger.application.dto.*
import com.teya.ledger.application.mapper.TransactionMapper
import com.teya.ledger.domain.factory.TransactionFactory
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Service

@Service
class LedgerService(
    private val ledgerRepository: LedgerRepository,
    private val transactionFactory: TransactionFactory,
    private val transactionMapper: TransactionMapper
) {
    fun recordTransaction(command: RecordTransactionCommand): TransactionResponse {
        val currentBalance = ledgerRepository.getBalance()

        // Create transaction domain object using factory (returns validated)
        val validatedTransaction = transactionFactory.create(
            type = command.type,
            amount = command.amount,
            currentBalance = currentBalance
        )

        // Save transaction and update balance
        ledgerRepository.save(validatedTransaction)

        val transaction = validatedTransaction.unwrap()
        return transactionMapper.toResponse(transaction)
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
                transactionMapper.toResponse(transaction)
            }
        )
    }
}
