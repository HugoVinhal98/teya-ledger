package com.teya.ledger.infrastructure.persistence

import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryLedgerRepositoryTest {

    private lateinit var repository: InMemoryLedgerRepository

    @BeforeEach
    fun setup() {
        repository = InMemoryLedgerRepository()
    }

    @Nested
    inner class GetBalance {

        @Test
        fun `when repository is created then balance is zero`() {
            // When
            val balance = repository.getBalance()

            // Then
            assertEquals(BigDecimal.ZERO, balance)
        }

        @Test
        fun `when a transaction is saved then getting the balance returns the updated value`() {
            // Given
            val transaction = TransactionTestDataFactory.createValidDeposit()
            repository.save(transaction)

            // When
            val balance = repository.getBalance()

            // Then
            assertEquals(transaction.unwrap().amount, balance)
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `when saving a transaction then it appears in history`() {
            // Given
            val transaction = TransactionTestDataFactory.createValidDeposit()

            // When
            repository.save(transaction)

            // Then
            val history = repository.getTransactionHistory()
            assertEquals(1, history.size)
            assertEquals(transaction.unwrap(), history[0])
        }

        @Test
        fun `when saving multiple transactions then all appear in history`() {
            // Given
            val transaction1 = TransactionTestDataFactory.createValidDeposit()
            val transaction2 = TransactionTestDataFactory.createValidDeposit()

            // When
            repository.save(transaction1)
            repository.save(transaction2)

            // Then
            val history = repository.getTransactionHistory()
            assertEquals(2, history.size)
        }

        @Test
        fun `when saving a transaction then balance is updated behind the scenes`() {
            // Given
            val transaction = TransactionTestDataFactory.createValidDeposit()

            // When
            repository.save(transaction)

            // Then
            assertEquals(transaction.unwrap().amount, repository.getBalance())
        }
    }

    @Nested
    inner class GetTransactionHistory {

        @Test
        fun `when no transactions exists then history is empty`() {
            // When
            val history = repository.getTransactionHistory()

            // Then
            assertTrue(history.isEmpty())
        }

        @Test
        fun `when getting transaction history then transactions are sorted by timestamp descending`() {
            // Given
            val now = LocalDateTime.now()
            val oldestTransaction = TransactionTestDataFactory.createValidDeposit(
                timestamp = now.minusMinutes(10)
            )
            val middleTransaction = TransactionTestDataFactory.createValidDeposit(
                timestamp = now.minusMinutes(5)
            )
            val newestTransaction = TransactionTestDataFactory.createValidDeposit(
                timestamp = now
            )

            // When
            repository.save(oldestTransaction)
            repository.save(middleTransaction)
            repository.save(newestTransaction)

            // Then
            val history = repository.getTransactionHistory()
            assertEquals(3, history.size)
            assertEquals(newestTransaction.unwrap().id, history[0].id)
            assertEquals(middleTransaction.unwrap().id, history[1].id)
            assertEquals(oldestTransaction.unwrap().id, history[2].id)
        }
    }
}
