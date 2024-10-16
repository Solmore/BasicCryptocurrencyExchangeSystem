package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.FiatCurrency
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FiatCurrencyRepository: JpaRepository<FiatCurrency, Long> {
    fun findByCode(code: String): FiatCurrency?
}