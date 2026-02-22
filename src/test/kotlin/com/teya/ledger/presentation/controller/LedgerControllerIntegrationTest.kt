package com.teya.ledger.presentation.controller

import com.teya.ledger.domain.model.TransactionType
import com.teya.ledger.testing.testdatafactories.RecordTransactionRequestTestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LedgerControllerIntegrationTest {

    @Autowired
    private lateinit var ledgerController: LedgerController

    @Test
    fun `when recording deposit transaction then returns transaction details`() {
        // Given
        val request = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
        )

        // When
        val response = ledgerController.recordTransaction(request)

        // Then
        assertThat(response.type).isEqualTo(TransactionType.DEPOSIT)
        assertThat(response.amount).isEqualTo(request.amount)
        assertThat(response.transactionId).isNotNull()
        assertThat(response.timestamp).isNotNull()
        assertThat(response.updatedBalance).isNotNull()
    }

    @Test
    fun `when recording withdrawal transaction then returns transaction details`() {
        // Given
        val depositRequest = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("200.00")
        )
        val withdrawalRequest = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.WITHDRAWAL,
            amount = BigDecimal("50.00")
        )

        // When
        ledgerController.recordTransaction(depositRequest)
        val response = ledgerController.recordTransaction(withdrawalRequest)

        // Then
        assertThat(response.type).isEqualTo(TransactionType.WITHDRAWAL)
        assertThat(response.amount).isEqualTo(withdrawalRequest.amount)
        assertThat(response.transactionId).isNotNull()
        assertThat(response.timestamp).isNotNull()
        assertThat(response.updatedBalance).isNotNull()
    }

    @Test
    fun `when getting balance without executing any operations before then returns zero`() {
        // When
        val response = ledgerController.getBalance()

        // Then
        assertThat(response.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `when getting transaction history without executing any operations before then returns empty list`() {
        // When
        val response = ledgerController.getTransactionHistory()

        // Then
        assertThat(response.transactions).isEmpty()
    }

    @Test
    fun `when getting transaction history then returns list of transactions`() {
        // Given
        val request = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
        )
        ledgerController.recordTransaction(request)

        // When
        val response = ledgerController.getTransactionHistory()

        // Then
        assertThat(response.transactions).isNotEmpty
        assertThat(response.transactions[0].transactionId).isNotNull()
        assertThat(response.transactions[0].type).isEqualTo(TransactionType.DEPOSIT)
        assertThat(response.transactions[0].amount).isEqualTo(request.amount)
        assertThat(response.transactions[0].timestamp).isNotNull()
        assertThat(response.transactions[0].updatedBalance).isEqualTo(request.amount)
    }

    @Test
    fun `when recording multiple transactions then balance is updated correctly`() {
        // Given
        val deposit1 = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("100.00")
        )
        val deposit2 = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("50.00")
        )
        val withdrawal = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.WITHDRAWAL,
            amount = BigDecimal("30.00")
        )

        // When
        ledgerController.recordTransaction(deposit1)
        ledgerController.recordTransaction(deposit2)
        ledgerController.recordTransaction(withdrawal)
        val finalBalance = ledgerController.getBalance().balance

        // Then
        assertThat(finalBalance).isEqualTo(BigDecimal("120.00"))
    }

    @Test
    fun `when recording transactions then history is sorted by timestamp descending`() {
        // Given
        val request1 = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("10.00")
        )
        val request2 = RecordTransactionRequestTestDataFactory.create(
            type = TransactionType.DEPOSIT,
            amount = BigDecimal("20.00")
        )

        // When
        ledgerController.recordTransaction(request1)
        Thread.sleep(10) // Small delay to ensure different timestamps
        ledgerController.recordTransaction(request2)

        val history = ledgerController.getTransactionHistory()

        // Then
        assertThat(history.transactions).hasSize(2)
        assertThat(history.transactions[0].amount).isEqualTo(request2.amount)
        assertThat(history.transactions[1].amount).isEqualTo(request1.amount)
    }
}
