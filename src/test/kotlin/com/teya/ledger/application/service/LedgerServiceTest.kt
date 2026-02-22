package com.teya.ledger.application.service

import com.teya.ledger.application.mapper.TransactionMapper
import com.teya.ledger.domain.factory.TransactionFactory
import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.domain.repository.LedgerRepository
import com.teya.ledger.testing.testdatafactories.RecordTransactionCommandTestDataFactory
import com.teya.ledger.testing.testdatafactories.TransactionResponseTestDataFactory
import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class LedgerServiceTest {

    private val ledgerRepository: LedgerRepository = mockk()
    private val transactionFactory: TransactionFactory = mockk()
    private val transactionMapper: TransactionMapper = mockk()

    private val ledgerService: LedgerService = LedgerService(ledgerRepository, transactionFactory, transactionMapper)

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
        val transaction = validatedTransaction.unwrap()
        val expectedResponse = TransactionResponseTestDataFactory.create()

        every { ledgerRepository.getBalance() } returns currentBalance
        every { transactionFactory.create(TransactionType.DEPOSIT, command.amount, currentBalance) } returns validatedTransaction
        every { ledgerRepository.save(validatedTransaction) } returns Unit
        every { transactionMapper.toResponse(transaction) } returns expectedResponse

        // When
        val response = ledgerService.recordTransaction(command)

        // Then
        assertThat(response).isEqualTo(expectedResponse)
        verify(exactly = 1) { ledgerRepository.getBalance() }
        verify(exactly = 1) { transactionFactory.create(TransactionType.DEPOSIT, command.amount, currentBalance) }
        verify(exactly = 1) { ledgerRepository.save(validatedTransaction) }
        verify(exactly = 1) { transactionMapper.toResponse(transaction) }
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
        val transaction = validatedTransaction.unwrap()
        val expectedResponse = TransactionResponseTestDataFactory.create()

        every { ledgerRepository.getBalance() } returns currentBalance
        every { transactionFactory.create(TransactionType.WITHDRAWAL, command.amount, currentBalance) } returns validatedTransaction
        every { ledgerRepository.save(validatedTransaction) } returns Unit
        every { transactionMapper.toResponse(transaction) } returns expectedResponse

        // When
        val response = ledgerService.recordTransaction(command)

        // Then
        assertThat(response).isEqualTo(expectedResponse)
        verify(exactly = 1) { ledgerRepository.getBalance() }
        verify(exactly = 1) { transactionFactory.create(TransactionType.WITHDRAWAL, command.amount, currentBalance) }
        verify(exactly = 1) { ledgerRepository.save(validatedTransaction) }
        verify(exactly = 1) { transactionMapper.toResponse(transaction) }
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
        val response1 = TransactionResponseTestDataFactory.create()
        val response2 = TransactionResponseTestDataFactory.create()

        every { ledgerRepository.getTransactionHistory() } returns listOf(transaction1, transaction2)
        every { transactionMapper.toResponse(transaction1) } returns response1
        every { transactionMapper.toResponse(transaction2) } returns response2

        // When
        val response = ledgerService.getTransactionHistory()

        // Then
        assertThat(response.transactions).hasSize(2)
        assertThat(response.transactions[0]).isEqualTo(response1)
        assertThat(response.transactions[1]).isEqualTo(response2)
        verify(exactly = 1) { ledgerRepository.getTransactionHistory() }
        verify(exactly = 1) { transactionMapper.toResponse(transaction1) }
        verify(exactly = 1) { transactionMapper.toResponse(transaction2) }
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
