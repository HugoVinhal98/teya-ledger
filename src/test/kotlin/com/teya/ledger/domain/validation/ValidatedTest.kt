package com.teya.ledger.domain.validation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidatedTest {

    @Test
    fun `when creating Validated instance then validate is called`() {
        // Given
        val validatable = mockk<Validatable>()
        every { validatable.validate() } returns Unit

        // When
        Validated(validatable)

        // Then
        verify(exactly = 1) { validatable.validate() }
    }

    @Test
    fun `when unwrapping Validated instance then validate is called and returns wrapped object`() {
        // Given
        val expectedValidatable = mockk<Validatable>()
        every { expectedValidatable.validate() } returns Unit
        val validated = Validated(expectedValidatable)

        // When
        val validatable = validated.unwrap()

        // Then
        assertThat(validatable).isEqualTo(expectedValidatable)
        // 1x for constructor
        // 1x for unwrap()
        verify(exactly = 2) { expectedValidatable.validate() }
    }

    @Test
    fun `when unwrapping Validated with object made invalid after instantiation then throws exception`() {
        // Given
        val expectedExceptionMessage = "property must not be blank!"
        val expectedValidatable = object : Validatable {
            var property = "property"

            override fun validate() {
                require(property.isNotBlank()) { expectedExceptionMessage }
            }
        }
        val validated = Validated(expectedValidatable)

        // When
        // make property invalid after instantiation of Validated
        expectedValidatable.property = " "

        // Then
        val exception = assertThrows<IllegalArgumentException> {
            validated.unwrap()
        }
        assertThat(exception.message).isEqualTo(expectedExceptionMessage)
    }
}
