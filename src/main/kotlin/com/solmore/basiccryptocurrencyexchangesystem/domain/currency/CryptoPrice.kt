package com.solmore.basiccryptocurrencyexchangesystem.domain.currency

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "crypto_price")
class CryptoPrice(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long,

    @ManyToOne
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto:CryptoCurrency,

    @ManyToOne
    @JoinColumn(name = "fiat_id", nullable = false)
    val fiat: FiatCurrency,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val updateAt: LocalDateTime = LocalDateTime.now()
)
