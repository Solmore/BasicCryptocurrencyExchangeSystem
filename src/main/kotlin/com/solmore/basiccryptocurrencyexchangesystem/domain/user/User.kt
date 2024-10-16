package com.solmore.basiccryptocurrencyexchangesystem.domain.user

import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String,
    val password: String,
    val roles: String = "USER",
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val wallets: List<Wallet> = mutableListOf()
)