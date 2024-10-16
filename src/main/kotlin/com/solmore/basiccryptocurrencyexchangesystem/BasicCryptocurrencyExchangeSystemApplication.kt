package com.solmore.basiccryptocurrencyexchangesystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EnableTransactionManagement
class BasicCryptocurrencyExchangeSystemApplication

fun main(args: Array<String>) {
    runApplication<BasicCryptocurrencyExchangeSystemApplication>(*args)
}
