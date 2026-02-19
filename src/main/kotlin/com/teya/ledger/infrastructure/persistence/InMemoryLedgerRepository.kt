package com.teya.ledger.infrastructure.persistence

import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.repository.LedgerRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class InMemoryLedgerRepository : LedgerRepository {
    private var balance: BigDecimal = BigDecimal.ZERO
    private val transactions: MutableList<Transaction> = mutableListOf()

    override fun save(transaction: Transaction) {
        transactions.add(transaction)

        balance = transaction.balanceAfter
    }

    override fun getBalance(): BigDecimal {
        return balance
    }

    override fun getTransactionHistory(): List<Transaction> {
        return transactions.sortedByDescending { it.timestamp }
    }
}
