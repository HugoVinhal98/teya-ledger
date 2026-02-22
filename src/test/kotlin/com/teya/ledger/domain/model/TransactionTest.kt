package com.teya.ledger.domain.model

import com.teya.ledger.domain.exception.InsufficientFundsException
import com.teya.ledger.domain.exception.InvalidAmountException
import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class TransactionTest {

    @Test
    fun `when creating transaction with positive amount and updated balance then validation passes`() {
        // Given
        val transaction = TransactionTestDataFactory.create(
            amount = BigDecimal("100.00"),
            updatedBalance = BigDecimal("100.00")
        )

        // When & Then
        assertDoesNotThrow {
            transaction.validate()
        }
    }

    @Test
    fun `when creating transaction with zero amount then validation passes`() {
        // Given
        val transaction = TransactionTestDataFactory.create(
            amount = BigDecimal.ZERO,
            updatedBalance = BigDecimal("50.00")
        )

        // When & Then
        assertDoesNotThrow {
            transaction.validate()
        }
    }

    @Test
    fun `when creating transaction with zero balance then validation passes`() {
        // Given
        val transaction = TransactionTestDataFactory.create(
            type = TransactionType.WITHDRAWAL,
            amount = BigDecimal("100.00"),
            updatedBalance = BigDecimal.ZERO
        )

        // When & Then
        assertDoesNotThrow {
            transaction.validate()
        }
    }

    @Test
    fun `when creating transaction with negative amount then throws an exception`() {
        // Given
        val negativeAmount = BigDecimal("-50.00")
        val transaction = TransactionTestDataFactory.create(
            amount = negativeAmount,
            updatedBalance = BigDecimal("100.00")
        )

        // When & Then
        val exception = assertThrows<InvalidAmountException> {
            transaction.validate()
        }
        assertThat(exception.message).isEqualTo("Invalid amount: the amount of $negativeAmount is not valid. Amount must be a non negative value.")
    }

    @Test
    fun `when creating transaction with very small negative amount then throws an exception`() {
        // Given
        val smallNegativeAmount = BigDecimal("-0.01")
        val transaction = TransactionTestDataFactory.create(
            amount = smallNegativeAmount,
            updatedBalance = BigDecimal("100.00")
        )

        // When & Then
        val exception = assertThrows<InvalidAmountException> {
            transaction.validate()
        }
        assertThat(exception.message).isEqualTo("Invalid amount: the amount of $smallNegativeAmount is not valid. Amount must be a non negative value.")
    }

    @Test
    fun `when creating transaction with negative updated balance then throws an exception`() {
        // Given
        val negativeBalance = BigDecimal("-50.00")
        val transaction = TransactionTestDataFactory.create(
            amount = BigDecimal("100.00"),
            updatedBalance = negativeBalance
        )

        // When & Then
        val exception = assertThrows<InsufficientFundsException> {
            transaction.validate()
        }
        assertThat(exception.message).isEqualTo("Insufficient funds: you don't have the funds for the requested withdrawal of ${transaction.amount}.")
    }

    @Test
    fun `when creating transaction with very small negative updated balance then throws an exception`() {
        // Given
        val smallNegativeBalance = BigDecimal("-0.01")
        val transaction = TransactionTestDataFactory.create(
            amount = BigDecimal("50.00"),
            updatedBalance = smallNegativeBalance
        )

        // When & Then
        val exception = assertThrows<InsufficientFundsException> {
            transaction.validate()
        }
        assertThat(exception.message).isEqualTo("Insufficient funds: you don't have the funds for the requested withdrawal of ${transaction.amount}.")
    }

    @Test
    fun `when creating transaction with negative amount and negative balance then throws invalid amount exception`() {
        // Given
        val negativeAmount = BigDecimal("-50.00")
        val negativeBalance = BigDecimal("-100.00")
        val transaction = TransactionTestDataFactory.create(
            amount = negativeAmount,
            updatedBalance = negativeBalance
        )

        // When & Then
        val exception = assertThrows<InvalidAmountException> {
            transaction.validate()
        }
        assertThat(exception.message).isEqualTo("Invalid amount: the amount of $negativeAmount is not valid. Amount must be a non negative value.")
    }
}
