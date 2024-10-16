package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.CryptoCurrency
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CryptoCurrencyRepository: JpaRepository<CryptoCurrency, Long> {
    fun findByCode(code: String): CryptoCurrency?
}