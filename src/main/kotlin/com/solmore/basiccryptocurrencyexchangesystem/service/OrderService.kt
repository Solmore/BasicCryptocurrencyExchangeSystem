package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.ResourceNotFound
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.Order
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderStatus
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderType
import com.solmore.basiccryptocurrencyexchangesystem.repository.CryptoCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.FiatCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderService(
    private val userService: UserService,
    private val walletService: WalletService,
    private val orderRepository: OrderRepository,
    private val orderMatchingService: OrderMatchingService,
    private val cryptoCurrencyRepository: CryptoCurrencyRepository,
    private val fiatCurrencyRepository: FiatCurrencyRepository
) {

    @Transactional
    fun createOrder(
        userId: Long,
        cryptoCode: String,
        fiatCode: String,
        orderType: OrderType,
        amount: BigDecimal,
        price: BigDecimal
    ): Order {
        if (amount <= BigDecimal.ZERO || price <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Amount and price must be greater than zero")
        }
        val user = userService.getById(userId)
            ?:throw ResourceNotFound("User Not Found")

        val cryptoCurrency = cryptoCurrencyRepository.findByCode(cryptoCode)
            ?: throw ResourceNotFound("Crypto $cryptoCode not found")
        val fiatCurrency = fiatCurrencyRepository.findByCode(fiatCode)
            ?: throw ResourceNotFound("Fiat $fiatCode not found")

        val wallet = when (orderType) {
            OrderType.BUY -> walletService.getWalletByUserIdAndCurrency(userId, fiatCode)
            OrderType.SELL -> walletService.getWalletByUserIdAndCurrency(userId, cryptoCode)
        }

        val requiredAmount = if (orderType == OrderType.BUY) price else amount
        if (wallet.balance < requiredAmount) {
            val currencyCode = if (orderType == OrderType.BUY) fiatCode else cryptoCode
            throw InsufficientFundsException("Not enough balance for $currencyCode")
        }

        val order = Order(
            user = user,
            crypto = cryptoCurrency,
            fiat = fiatCurrency,
            type = orderType,
            amount = amount,
            price = price,
            status = OrderStatus.OPEN
        )

        val savedOrder = orderRepository.save(order)
        orderMatchingService.matchOrders(savedOrder)

        return savedOrder
    }

    fun getOrder(orderId: Long): Order {
        return orderRepository.findById(orderId)
            .orElseThrow { ResourceNotFound("Order with ID $orderId not found") }
    }
}