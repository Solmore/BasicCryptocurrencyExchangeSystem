package com.solmore.basiccryptocurrencyexchangesystem.web.controller

import com.solmore.basiccryptocurrencyexchangesystem.service.OrderMatchingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order-matching")
//TODO:посмотреть как строиться конструкция в kotlin, пример ниже
class OrderMatchingController(
    private val orderMatchingService: OrderMatchingService
) {

    @PostMapping("/match/{orderId}")
    fun matchOrder(@PathVariable orderId: Long): ResponseEntity<String> {
        val order = orderMatchingService.matchOrderById(orderId)
        return ResponseEntity.ok("Order matching initiated for order $orderId")
    }
}