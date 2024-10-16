package com.solmore.basiccryptocurrencyexchangesystem.domain.currency

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column


@Entity
@Table(name = "fiat_currencies")
class FiatCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val symbol: String
)
