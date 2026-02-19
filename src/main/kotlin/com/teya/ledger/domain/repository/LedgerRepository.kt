package com.teya.ledger.domain.repository

import com.teya.ledger.domain.model.Transaction
import java.math.BigDecimal

interface LedgerRepository {
    fun save(transaction: Transaction)
    fun getBalance(): BigDecimal
    fun getTransactionHistory(): List<Transaction>
}
