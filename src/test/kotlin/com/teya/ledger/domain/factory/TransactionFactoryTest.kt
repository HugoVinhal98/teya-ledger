package com.teya.ledger.domain.factory

import com.teya.ledger.domain.model.TransactionType
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TransactionFactoryTest {

    private val transactionFactory = TransactionFactory()

    @Test
    fun `when creating a deposit then all fields are mapped correctly`() {
        // Given
        val depositAmount = BigDecimal("50.00")
        val type = TransactionType.DEPOSIT
        val currentBalance = BigDecimal("100.00")

        // When
        val validatedTransaction = transactionFactory.create(
            type = type,
            amount = depositAmount,
            currentBalance = currentBalance
        )

        // Then
        val transaction = validatedTransaction.unwrap()
        assertNotNull(transaction)
        assertEquals(type, transaction.type)
        assertEquals(depositAmount, transaction.amount)
        assertNotNull(transaction.id)
        assertNotNull(transaction.timestamp)
        assertEquals( BigDecimal("150.00"), transaction.updatedBalance)
    }

    @Test
    fun `when creating a withdrawal then all fields are mapped correctly`() {
        // Given
        val withdrawalAmount = BigDecimal("50.00")
        val type = TransactionType.WITHDRAWAL
        val currentBalance = BigDecimal("100.00")

        // When
        val validatedTransaction = transactionFactory.create(
            type = type,
            amount = withdrawalAmount,
            currentBalance = currentBalance
        )

        // Then
        val transaction = validatedTransaction.unwrap()
        assertNotNull(transaction)
        assertEquals(type, transaction.type)
        assertEquals(withdrawalAmount, transaction.amount)
        assertNotNull(transaction.id)
        assertNotNull(transaction.timestamp)
        assertEquals( BigDecimal("50.00"), transaction.updatedBalance)
    }
}
