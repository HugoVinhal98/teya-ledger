package com.teya.ledger.presentation.controller

import com.teya.ledger.application.dto.BalanceResponse
import com.teya.ledger.application.dto.RecordTransactionRequest
import com.teya.ledger.application.dto.RecordTransactionResponse
import com.teya.ledger.application.dto.TransactionHistoryResponse
import com.teya.ledger.application.service.LedgerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
            ApiResponse(responseCode = "400", description = "Insufficient funds for withdrawal")
        ]
    )
    fun recordTransaction(@RequestBody request: RecordTransactionRequest): RecordTransactionResponse {
        return ledgerService.recordTransaction(request)
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
}
