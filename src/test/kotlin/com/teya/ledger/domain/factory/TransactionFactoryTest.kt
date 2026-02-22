package com.teya.ledger.domain.factory

import com.teya.ledger.domain.model.TransactionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

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
        assertThat(transaction).isNotNull()
        assertThat(transaction.type).isEqualTo(type)
        assertThat(transaction.amount).isEqualTo(depositAmount)
        assertThat(transaction.id).isNotNull()
        assertThat(transaction.timestamp).isNotNull()
        assertThat(transaction.updatedBalance).isEqualTo(BigDecimal("150.00"))
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
        assertThat(transaction).isNotNull()
        assertThat(transaction.type).isEqualTo(type)
        assertThat(transaction.amount).isEqualTo(withdrawalAmount)
        assertThat(transaction.id).isNotNull()
        assertThat(transaction.timestamp).isNotNull()
        assertThat(transaction.updatedBalance).isEqualTo(BigDecimal("50.00"))
    }
}
