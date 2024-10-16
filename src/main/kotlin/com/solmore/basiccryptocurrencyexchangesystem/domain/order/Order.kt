package com.solmore.basiccryptocurrencyexchangesystem.domain.order

import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.CryptoCurrency
import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.FiatCurrency
import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto: CryptoCurrency,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiat_id", nullable = false)
    val fiat: FiatCurrency,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: OrderType, // BUY or SELL

    @Column(nullable = false)
    var amount: BigDecimal,

    @Column(nullable = false)
    val price: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.OPEN,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}