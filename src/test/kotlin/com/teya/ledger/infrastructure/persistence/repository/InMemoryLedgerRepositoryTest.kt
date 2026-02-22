package com.teya.ledger.infrastructure.persistence.repository

import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryLedgerRepositoryTest {

    private lateinit var repository: InMemoryLedgerRepository

    @BeforeEach
    fun setUp() {
        repository = InMemoryLedgerRepository()
    }

    @Test
    fun `when getting balance with no transactions then returns zero`() {
        // When
        val balance = repository.getBalance()

        // Then
        assertEquals(BigDecimal.ZERO, balance)
    }

    @Test
    fun `when getting transaction history with no transactions then returns empty list`() {
        // When
        val history = repository.getTransactionHistory()

        // Then
        assertTrue(history.isEmpty())
    }

    @Test
    fun `when saving a deposit transaction then balance is updated`() {
        // Given
        val transaction = TransactionTestDataFactory.createValid()

        // When
        repository.save(transaction)

        // Then
        assertEquals(transaction.unwrap().updatedBalance, repository.getBalance())
    }

    @Test
    fun `when saving a withdrawal transaction then balance is updated`() {
        // Given
        val withdrawalTransaction = TransactionTestDataFactory.createValid(
            type = TransactionType.WITHDRAWAL,
        )

        // When
        repository.save(withdrawalTransaction)

        // Then
        assertEquals(withdrawalTransaction.unwrap().updatedBalance, repository.getBalance())
    }

    @Test
    fun `when saving multiple transactions then balance reflects the last transaction`() {
        // Given
        val firstTransaction = TransactionTestDataFactory.createValid(
            updatedBalance = BigDecimal("100.00")
        )
        val secondTransaction = TransactionTestDataFactory.createValid(
            updatedBalance = BigDecimal("150.00")
        )
        val thirdTransaction = TransactionTestDataFactory.createValid(
            updatedBalance = BigDecimal("120.00")
        )

        // When
        repository.save(firstTransaction)
        repository.save(secondTransaction)
        repository.save(thirdTransaction)

        // Then
        assertEquals(BigDecimal("120.00"), repository.getBalance())
    }

    @Test
    fun `when saving a transaction then it appears in transaction history`() {
        // Given
        val transaction = TransactionTestDataFactory.createValid()

        // When
        repository.save(transaction)
        val history = repository.getTransactionHistory()

        // Then
        assertEquals(1, history.size)
        assertEquals(transaction.unwrap(), history[0])
    }

    @Test
    fun `when saving multiple transactions then history is sorted by timestamp descending`() {
        // Given
        val now = LocalDateTime.now()
        val oldTransaction = TransactionTestDataFactory.createValid(
            timestamp = now.minusHours(2),
        )
        val middleTransaction = TransactionTestDataFactory.createValid(
            timestamp = now.minusHours(1),
        )
        val recentTransaction = TransactionTestDataFactory.createValid(
            timestamp = now,
        )

        // When
        repository.save(oldTransaction)
        repository.save(middleTransaction)
        repository.save(recentTransaction)
        val history = repository.getTransactionHistory()

        // Then
        assertEquals(recentTransaction.unwrap(), history[0])
        assertEquals(middleTransaction.unwrap(), history[1])
        assertEquals(oldTransaction.unwrap(), history[2])
    }

    @Test
    fun `when saving transactions with same timestamp then all appear in history`() {
        // Given
        val timestamp = LocalDateTime.now()
        val firstTransaction = TransactionTestDataFactory.createValid(
            timestamp = timestamp,
        )
        val secondTransaction = TransactionTestDataFactory.createValid(
            timestamp = timestamp,
        )

        // When
        repository.save(firstTransaction)
        repository.save(secondTransaction)
        val history = repository.getTransactionHistory()

        // Then
        assertThat(history).containsExactlyInAnyOrder(firstTransaction.unwrap(), secondTransaction.unwrap())
    }

    @Test
    fun `when modifying returned transaction history then repository internal state is not affected`() {
        // Given
        val transaction = TransactionTestDataFactory.createValid(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("100.00"),
            updatedBalance = BigDecimal("100.00")
        )
        repository.save(transaction)

        // When
        val history = repository.getTransactionHistory().toMutableList()
        history.clear()

        // Then
        val actualHistory = repository.getTransactionHistory()
        assertEquals(1, actualHistory.size)
        assertEquals(transaction.unwrap(), actualHistory[0])
    }
}
