package com.teya.ledger.domain.repository

import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.model.TransactionType
import java.math.BigDecimal

interface LedgerRepository {
    fun recordTransaction(type: TransactionType, amount: BigDecimal): Transaction
    fun getBalance(): BigDecimal
    fun getTransactionHistory(): List<Transaction>
}
