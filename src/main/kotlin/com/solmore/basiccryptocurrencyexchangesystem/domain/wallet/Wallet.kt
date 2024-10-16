package com.solmore.basiccryptocurrencyexchangesystem.domain.wallet

import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(name = "wallets")
data class Wallet(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    val currency: String,
    var balance: BigDecimal = BigDecimal.ZERO
)