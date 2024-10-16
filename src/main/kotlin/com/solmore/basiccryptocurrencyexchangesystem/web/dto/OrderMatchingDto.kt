package com.solmore.basiccryptocurrencyexchangesystem.web.dto

import java.math.BigDecimal

data class OrderMatchingDto(
    val buyOrderId: Long,
    val sellOrderId: Long,
    val matchedAmount: BigDecimal,
    val matchedPrice: BigDecimal
)