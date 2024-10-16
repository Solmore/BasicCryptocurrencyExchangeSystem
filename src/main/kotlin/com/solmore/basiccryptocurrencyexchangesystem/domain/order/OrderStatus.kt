package com.solmore.basiccryptocurrencyexchangesystem.domain.order

/*
enum class OrderStatus {
    OPEN,
    PARTIALLY_FILLED,
    CLOSED
}*/
enum class OrderStatus(val value: String) {
    OPEN("OPEN"),
    PARTIALLY_FILLED("PARTIALLY_FILLED"),
    CLOSED("CLOSED")
}