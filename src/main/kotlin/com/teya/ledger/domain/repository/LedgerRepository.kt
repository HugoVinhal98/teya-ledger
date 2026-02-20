package com.teya.ledger.domain.repository

import com.teya.ledger.domain.model.Transaction
import com.teya.ledger.domain.validation.Validated
import java.math.BigDecimal

interface LedgerRepository {
    fun save(transaction: Validated<Transaction>)
    fun getBalance(): BigDecimal
    fun updateBalance(balance: BigDecimal)
    fun getTransactionHistory(): List<Transaction>
}
