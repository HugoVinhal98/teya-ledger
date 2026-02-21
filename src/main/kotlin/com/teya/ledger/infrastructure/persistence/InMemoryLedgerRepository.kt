package com.teya.ledger.infrastructure.persistence

import com.teya.ledger.domain.exception.InsufficientFundsException
import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import com.teya.ledger.domain.validation.Validated
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryLedgerRepository : LedgerRepository {
    private var balance: BigDecimal = BigDecimal.ZERO
    private val transactions: MutableList<Transaction> = mutableListOf()

    override fun save(transaction: Validated<Transaction>) {
        val unwrappedTransaction = transaction.unwrap()
        val updatedBalance = when (unwrappedTransaction.type) {
            TransactionType.DEPOSIT -> balance.add(unwrappedTransaction.amount)
            TransactionType.WITHDRAWAL -> balance.subtract(unwrappedTransaction.amount)
        }

        if (updatedBalance < BigDecimal.ZERO) {
            throw InsufficientFundsException(unwrappedTransaction.amount)
        }

        balance = updatedBalance
        transactions.add(unwrappedTransaction)
    }

    override fun getBalance(): BigDecimal {
        return balance
    }

    override fun getTransactionHistory(): List<Transaction> {
        return transactions.sortedByDescending { it.timestamp }
    }
}
