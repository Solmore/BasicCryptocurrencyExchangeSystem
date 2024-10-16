package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.Order
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderStatus
import com.solmore.basiccryptocurrencyexchangesystem.repository.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderMatchingService(
    private val orderRepository: OrderRepository,
    private val walletService: WalletService
) {
    @Transactional
    fun matchOrderById(orderId: Long): Order? {
        val order = orderRepository.findByIdOrNull(orderId) ?: return null
        matchOrders(order)
        return order
    }

    @Transactional
    fun matchOrders(newOrder: Order) {
        val oppositeOrders = orderRepository.findMatchingOrders(
            newOrder.crypto.code,
            newOrder.type.opposite(),
            newOrder.price,
            newOrder.status
        )
        //TODO: перевести в stream api и вынести orderRepository(чтобы была одна операция save, а не много)
        for (oppositeOrder in oppositeOrders) {
            if (newOrder.status == OrderStatus.CLOSED) break

            val matchedAmount = minOf(newOrder.amount, oppositeOrder.amount)

            processOrderMatch(newOrder, oppositeOrder, matchedAmount)

            if (newOrder.amount == BigDecimal.ZERO) {
                newOrder.status = OrderStatus.CLOSED
            } else {
                newOrder.status = OrderStatus.PARTIALLY_FILLED
            }

            if (oppositeOrder.amount == BigDecimal.ZERO) {
                oppositeOrder.status = OrderStatus.CLOSED
            } else {
                oppositeOrder.status = OrderStatus.PARTIALLY_FILLED
            }

            orderRepository.save(newOrder)
            orderRepository.save(oppositeOrder)
        }
    }

    private fun processOrderMatch(buyOrder: Order, sellOrder: Order, amount: BigDecimal) {
        val totalFiatAmount = amount * sellOrder.price

        // Check if both buyer and seller have sufficient funds
        if (!hasSufficientBalance(buyOrder.user.id, buyOrder.fiat.code, totalFiatAmount)) {
            throw InsufficientFundsException("Buyer does not have enough fiat (${buyOrder.fiat.code}) to complete the order.")
        }
        if (!hasSufficientBalance(sellOrder.user.id, sellOrder.crypto.code, amount)) {
            throw InsufficientFundsException("Seller does not have enough crypto (${sellOrder.crypto.code}) to complete the order.")
        }

        updateWalletBalance(sellOrder.user.id, sellOrder.crypto.code, amount.negate())
        updateWalletBalance(buyOrder.user.id, buyOrder.crypto.code, amount)

        updateWalletBalance(buyOrder.user.id, buyOrder.fiat.code, totalFiatAmount.negate())
        updateWalletBalance(sellOrder.user.id, sellOrder.fiat.code, totalFiatAmount)
    }

    private fun hasSufficientBalance(userId: Long, currencyCode: String, requiredAmount: BigDecimal): Boolean {
        val wallet = walletService.getWalletByUserIdAndCurrency(userId, currencyCode)
        return wallet.balance >= requiredAmount
    }

    private fun updateWalletBalance(userId: Long, currencyCode: String, amount: BigDecimal) {
        val wallet = walletService.getWalletByUserIdAndCurrency(userId, currencyCode)
        walletService.updateWalletBalance(wallet.id, amount)
    }
}