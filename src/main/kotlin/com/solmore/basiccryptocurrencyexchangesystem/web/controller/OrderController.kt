package com.solmore.basiccryptocurrencyexchangesystem.web.controller

import com.solmore.basiccryptocurrencyexchangesystem.domain.order.Order
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderType
import com.solmore.basiccryptocurrencyexchangesystem.service.OrderService
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.OrderDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{userId}/create")
    fun createOrder(
        @PathVariable userId: Long,
        @RequestParam cryptoCode: String,
        @RequestParam fiatCode: String,
        @RequestParam orderType: OrderType,
        @RequestParam amount: BigDecimal,
        @RequestParam price: BigDecimal
    ): ResponseEntity<OrderDto> {
        val order = orderService.createOrder(userId, cryptoCode, fiatCode, orderType, amount, price)
        return ResponseEntity.ok(order.toDto())
    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: Long): ResponseEntity<OrderDto> {
        val order = orderService.getOrder(orderId)
        return ResponseEntity.ok(order.toDto())
    }
    //TODO:Посмотреть насчет функции toDto как работает
    fun Order.toDto(): OrderDto {
        return OrderDto(
            id = this.id,
            userId = this.user.id,
            cryptoCode = this.crypto.code,
            fiatCode = this.fiat.code,
            amount = this.amount,
            price = this.price,
            status = this.status
        )
    }
}