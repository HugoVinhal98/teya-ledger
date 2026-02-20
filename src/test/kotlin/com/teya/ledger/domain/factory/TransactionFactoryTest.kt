package com.teya.ledger.domain.factory

import com.teya.ledger.domain.model.TransactionType
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TransactionFactoryTest {

    private val transactionFactory = TransactionFactory()

    @Test
    fun `when creating a transaction then all fields are mapped correctly`() {
        // Given
        val depositAmount = BigDecimal("50.00")
        val type = TransactionType.entries.random()

        // When
        val validatedTransaction = transactionFactory.create(
            type = type,
            amount = depositAmount,
        )

        // Then
        val transaction = validatedTransaction.unwrap()
        assertNotNull(transaction)
        assertEquals(type, transaction.type)
        assertEquals(depositAmount, transaction.amount)
        assertNotNull(transaction.id)
        assertNotNull(transaction.timestamp)
    }
}
