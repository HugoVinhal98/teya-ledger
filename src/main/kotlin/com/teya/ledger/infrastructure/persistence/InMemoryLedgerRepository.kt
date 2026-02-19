package com.teya.ledger.infrastructure.persistence

import com.teya.ledger.domain.exception.InsufficientFundsException
import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Repository
class InMemoryLedgerRepository : LedgerRepository {
    private var balance: BigDecimal = BigDecimal.ZERO
    private val transactions: MutableList<Transaction> = mutableListOf()

    override fun recordTransaction(type: TransactionType, amount: BigDecimal): Transaction {
        if (type == TransactionType.WITHDRAWAL && balance < amount) {
            throw InsufficientFundsException(currentBalance = balance, requestedAmount = amount)
        }

        val newBalance = when (type) {
            TransactionType.DEPOSIT -> balance.add(amount)
            TransactionType.WITHDRAWAL -> balance.subtract(amount)
        }

        val transaction = Transaction(
            id = UUID.randomUUID(),
            type = type,
            amount = amount,
            timestamp = LocalDateTime.now(),
            balanceAfter = newBalance
        )

        balance = newBalance
        transactions.add(transaction)

        return transaction
    }

    override fun getBalance(): BigDecimal {
        return balance
    }

    override fun getTransactionHistory(): List<Transaction> {
        return transactions.sortedByDescending { it.timestamp }
    }
}
