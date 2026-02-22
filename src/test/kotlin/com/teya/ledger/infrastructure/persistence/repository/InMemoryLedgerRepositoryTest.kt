package com.teya.ledger.infrastructure.persistence.repository

import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

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
        assertThat(balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `when getting transaction history with no transactions then returns empty list`() {
        // When
        val history = repository.getTransactionHistory()

        // Then
        assertThat(history).isEmpty()
    }

    @Test
    fun `when saving a deposit transaction then balance is updated`() {
        // Given
        val transaction = TransactionTestDataFactory.createValid()

        // When
        repository.save(transaction)

        // Then
        assertThat(repository.getBalance()).isEqualTo(transaction.unwrap().updatedBalance)
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
        assertThat(repository.getBalance()).isEqualTo(withdrawalTransaction.unwrap().updatedBalance)
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
        assertThat(repository.getBalance()).isEqualTo(BigDecimal("120.00"))
    }

    @Test
    fun `when saving a transaction then it appears in transaction history`() {
        // Given
        val transaction = TransactionTestDataFactory.createValid()

        // When
        repository.save(transaction)
        val history = repository.getTransactionHistory()

        // Then
        assertThat(history).hasSize(1)
        assertThat(history[0]).isEqualTo(transaction.unwrap())
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
        assertThat(history[0]).isEqualTo(recentTransaction.unwrap())
        assertThat(history[1]).isEqualTo(middleTransaction.unwrap())
        assertThat(history[2]).isEqualTo(oldTransaction.unwrap())
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
        assertThat(actualHistory).hasSize(1)
        assertThat(actualHistory[0]).isEqualTo(transaction.unwrap())
    }
}
