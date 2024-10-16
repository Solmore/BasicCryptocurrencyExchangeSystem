package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : JpaRepository<Wallet,Long> {
    fun findByUserIdAndCurrency(userId: Long, currency: String): Wallet?
    fun findAllByUserId(userId: Long): List<Wallet>
}