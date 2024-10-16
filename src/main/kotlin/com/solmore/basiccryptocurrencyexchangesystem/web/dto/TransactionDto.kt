package com.solmore.basiccryptocurrencyexchangesystem.web.dto


import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    val id: Long?,
    val userId: Long,
    val amount: BigDecimal,
    val type: TransactionType,
    val createdAt: LocalDateTime
)
