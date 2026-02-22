/* Copyright 2022 - 2026 Trade Republic */
package com.teya.ledger.domain.validation

data class Validated<T : Validatable>(
    private val validated: T,
) {
    init {
        validated.validate()
    }

    fun unwrap(): T = validated.also { validated.validate() }
}
