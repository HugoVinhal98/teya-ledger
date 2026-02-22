package com.teya.ledger.presentation.controller

import com.teya.ledger.application.command.RecordTransactionCommand
import com.teya.ledger.application.dto.BalanceResponse
import com.teya.ledger.application.dto.TransactionHistoryResponse
import com.teya.ledger.application.dto.TransactionResponse
import com.teya.ledger.application.service.LedgerService
import com.teya.ledger.presentation.dto.RecordTransactionRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Tag(name = "Ledger", description = "Ledger management APIs")
class LedgerController(
    private val ledgerService: LedgerService
) {
    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Record a transaction", description = "Record a deposit or withdrawal transaction")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Transaction recorded successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request or insufficient funds")
        ]
    )
    fun recordTransaction(@Valid @RequestBody request: RecordTransactionRequest): TransactionResponse {
        val command = toCommand(request)
        return ledgerService.recordTransaction(command)
    }

    @GetMapping("/balance")
    @Operation(summary = "Get current balance", description = "Retrieve the current balance of the ledger")
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    fun getBalance(): BalanceResponse {
        return ledgerService.getBalance()
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get transaction history", description = "Retrieve all transactions sorted by timestamp (newest first)")
    @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully")
    fun getTransactionHistory(): TransactionHistoryResponse {
        return ledgerService.getTransactionHistory()
    }

    private fun toCommand(request: RecordTransactionRequest): RecordTransactionCommand {
        return RecordTransactionCommand(
            type = request.type,
            amount = request.amount
        )
    }
}
