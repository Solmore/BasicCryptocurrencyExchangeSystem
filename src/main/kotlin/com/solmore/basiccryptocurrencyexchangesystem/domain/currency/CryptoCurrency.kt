package com.solmore.basiccryptocurrencyexchangesystem.domain.currency

import jakarta.persistence.*

@Entity
@Table(name = "crypto_currencies")
class CryptoCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false, unique = true)
    val code:String,

    @Column(nullable = false)
    val name:String,

    @OneToMany(mappedBy = "crypto")
    val prices: List<CryptoPrice> = mutableListOf()
)
