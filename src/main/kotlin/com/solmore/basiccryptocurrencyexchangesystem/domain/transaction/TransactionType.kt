package com.solmore.basiccryptocurrencyexchangesystem.domain.transaction

/*
enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL
}*/

enum class TransactionType(val value: String) {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL");
}