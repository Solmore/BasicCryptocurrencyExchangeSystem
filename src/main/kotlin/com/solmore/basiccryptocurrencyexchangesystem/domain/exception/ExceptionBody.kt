package com.solmore.basiccryptocurrencyexchangesystem.domain.exception

data class ExceptionBody(
    val message: String,
    val error: Map<String, String> = emptyMap()
){
    constructor(message: String) : this(message, emptyMap())
}
