package com.solmore.basiccryptocurrencyexchangesystem.web.dto

import java.math.BigDecimal

data class WalletDto(
    val id: Long?,
    val userId: Long,
    val currency: String,
    val balance: BigDecimal
)
