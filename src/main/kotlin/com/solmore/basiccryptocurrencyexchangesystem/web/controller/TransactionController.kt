package com.solmore.basiccryptocurrencyexchangesystem.web.controller

import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.Transaction
import com.solmore.basiccryptocurrencyexchangesystem.service.TransactionService
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.TransactionDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping("/{userId}/deposit")
    fun deposit(
        @PathVariable userId: Long,
        @RequestParam currencyCode: String,
        @RequestParam amount: BigDecimal
    ): ResponseEntity<TransactionDto> {
        val transaction = transactionService.deposit(userId, currencyCode, amount)
        return ResponseEntity.ok(transaction.toDto())
    }

    @PostMapping("/{userId}/withdraw")
    fun withdraw(
        @PathVariable userId: Long,
        @RequestParam currencyCode: String,
        @RequestParam amount: BigDecimal
    ): ResponseEntity<TransactionDto> {
        val transaction = transactionService.withdraw(userId, currencyCode, amount)
        return ResponseEntity.ok(transaction.toDto())
    }

    fun Transaction.toDto(): TransactionDto {
        return TransactionDto(
            id = this.id,
            userId = this.wallet.user.id,
            amount = this.amount,
            type = this.type,
            createdAt = this.createdAt)
    }
}