package com.solmore.basiccryptocurrencyexchangesystem.domain.order

/*
enum class OrderType {
    BUY,
    SELL;

    fun opposite(): OrderType {
        return when (this) {
            BUY -> SELL
            SELL -> BUY
        }
    }
}*/

enum class OrderType(val value: String) {
    BUY("BUY"),
    SELL("SELL");

    fun opposite(): OrderType {
        return when (this) {
            BUY -> SELL
            SELL -> BUY
        }
    }
}
