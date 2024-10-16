package com.solmore.basiccryptocurrencyexchangesystem.web.dto

import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderStatus
import java.math.BigDecimal

data class OrderDto(
    val id: Long?,
    val userId: Long,
    val cryptoCode: String, // The cryptocurrency involved (e.g., BTC, ETH)
    val fiatCode: String, // The fiat currency involved (e.g., USD, EUR)
    val amount: BigDecimal, // Amount of cryptocurrency to buy/sell
    val price: BigDecimal, // Price per unit of cryptocurrency
    val status: OrderStatus // Enum: OPEN, PARTIALLY_FILLED, CLOSED
)
