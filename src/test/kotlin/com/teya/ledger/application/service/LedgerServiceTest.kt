package com.teya.ledger.application.service

import com.teya.ledger.domain.factory.TransactionFactory
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import com.teya.ledger.testing.testdatafactories.RecordTransactionCommandTestDataFactory
import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class LedgerServiceTest {

    private val ledgerRepository: LedgerRepository = mockk()
    private val transactionFactory: TransactionFactory = mockk()

    private val ledgerService: LedgerService = LedgerService(ledgerRepository, transactionFactory)

    @Test
    fun `when recording deposit transaction then transaction is saved and response is returned`() {
        // Given
        val command = RecordTransactionCommandTestDataFactory.create(
            type = TransactionType.DEPOSIT,
        )
        val currentBalance = BigDecimal("50.00")
        val validatedTransaction = TransactionTestDataFactory.createValid(
            type = TransactionType.DEPOSIT,
        )

        every { ledgerRepository.getBalance() } returns currentBalance
        every { transactionFactory.create(TransactionType.DEPOSIT, command.amount, currentBalance) } returns validatedTransaction
        every { ledgerRepository.save(validatedTransaction) } returns Unit

        // When
        val response = ledgerService.recordTransaction(command)

        // Then
        assertThat(response.type).isEqualTo(TransactionType.DEPOSIT)
        assertThat(response.amount).isEqualTo(validatedTransaction.unwrap().amount)
        assertThat(response.transactionId).isEqualTo(validatedTransaction.unwrap().id)
        verify(exactly = 1) { ledgerRepository.getBalance() }
        verify(exactly = 1) { transactionFactory.create(TransactionType.DEPOSIT, command.amount, currentBalance) }
        verify(exactly = 1) { ledgerRepository.save(validatedTransaction) }
    }

    @Test
    fun `when recording withdrawal transaction then transaction is saved and response is returned`() {
        // Given
        val command = RecordTransactionCommandTestDataFactory.create(
            type = TransactionType.WITHDRAWAL,
        )
        val currentBalance = BigDecimal("100.00")
        val validatedTransaction = TransactionTestDataFactory.createValid(
            type = TransactionType.WITHDRAWAL,
        )

        every { ledgerRepository.getBalance() } returns currentBalance
        every { transactionFactory.create(TransactionType.WITHDRAWAL, command.amount, currentBalance) } returns validatedTransaction
        every { ledgerRepository.save(validatedTransaction) } returns Unit

        // When
        val response = ledgerService.recordTransaction(command)

        // Then
        assertThat(response.type).isEqualTo(TransactionType.WITHDRAWAL)
        assertThat(response.amount).isEqualTo(validatedTransaction.unwrap().amount)
        assertThat(response.transactionId).isEqualTo(validatedTransaction.unwrap().id)
        verify(exactly = 1) { ledgerRepository.getBalance() }
        verify(exactly = 1) { transactionFactory.create(TransactionType.WITHDRAWAL, command.amount, currentBalance) }
        verify(exactly = 1) { ledgerRepository.save(validatedTransaction) }
    }

    @Test
    fun `when getting balance then returns balance from repository`() {
        // Given
        val expectedBalance = BigDecimal("250.50")
        every { ledgerRepository.getBalance() } returns expectedBalance

        // When
        val response = ledgerService.getBalance()

        // Then
        assertThat(response.balance).isEqualTo(expectedBalance)
        verify(exactly = 1) { ledgerRepository.getBalance() }
    }

    @Test
    fun `when getting balance with zero balance then returns zero`() {
        // Given
        every { ledgerRepository.getBalance() } returns BigDecimal.ZERO

        // When
        val response = ledgerService.getBalance()

        // Then
        assertThat(response.balance).isEqualTo(BigDecimal.ZERO)
        verify(exactly = 1) { ledgerRepository.getBalance() }
    }

    @Test
    fun `when getting transaction history then returns mapped transactions`() {
        // Given
        val transaction1 = TransactionTestDataFactory.create()
        val transaction2 = TransactionTestDataFactory.create()
        every { ledgerRepository.getTransactionHistory() } returns listOf(transaction1, transaction2)

        // When
        val response = ledgerService.getTransactionHistory()

        // Then
        assertThat(response.transactions).hasSize(2)
        assertThat(response.transactions[0].transactionId).isEqualTo(transaction1.id)
        assertThat(response.transactions[0].type).isEqualTo(transaction1.type)
        assertThat(response.transactions[0].amount).isEqualTo(transaction1.amount)
        assertThat(response.transactions[1].transactionId).isEqualTo(transaction2.id)
        assertThat(response.transactions[1].type).isEqualTo(transaction2.type)
        assertThat(response.transactions[1].amount).isEqualTo(transaction2.amount)
        verify(exactly = 1) { ledgerRepository.getTransactionHistory() }
    }

    @Test
    fun `when getting transaction history with empty history then returns empty list`() {
        // Given
        every { ledgerRepository.getTransactionHistory() } returns emptyList()

        // When
        val response = ledgerService.getTransactionHistory()

        // Then
        assertThat(response.transactions).isEmpty()
        verify(exactly = 1) { ledgerRepository.getTransactionHistory() }
    }
}
