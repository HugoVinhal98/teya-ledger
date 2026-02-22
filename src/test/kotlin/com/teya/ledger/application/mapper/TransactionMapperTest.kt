package com.teya.ledger.application.mapper

import com.teya.ledger.testing.testdatafactories.TransactionTestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransactionMapperTest {

    private lateinit var mapper: TransactionMapper

    @BeforeEach
    fun setUp() {
        mapper = TransactionMapper()
    }

    @Test
    fun `when mapping deposit transaction then all fields are mapped correctly`() {
        // Given
        val transaction = TransactionTestDataFactory.create()

        // When
        val response = mapper.toResponse(transaction)

        // Then
        assertThat(response.transactionId).isEqualTo(transaction.id)
        assertThat(response.type).isEqualTo(transaction.type)
        assertThat(response.amount).isEqualTo(transaction.amount)
        assertThat(response.timestamp).isEqualTo(transaction.timestamp)
        assertThat(response.updatedBalance).isEqualTo(transaction.updatedBalance)
    }
}
