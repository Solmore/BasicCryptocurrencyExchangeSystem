package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.order.Order
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderStatus
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface OrderRepository: JpaRepository<Order,Long> {
    fun findByUserId(userId: Long): List<Order>
    fun findByCryptoCodeAndFiatCodeAndTypeAndStatus(
        cryptoCode: String,
        fiatCode: String,
        type: OrderType,
        status: OrderStatus
    ): List<Order>

    /*@Query("""
        SELECT o 
        FROM Order o 
        JOIN o.crypto c 
        WHERE c.code = :cryptoCode 
        AND o.type = :type 
        AND o.price <= :price 
        AND o.status = 'OPEN'
    """)
    fun findMatchingOrders(
        @Param("cryptoCode") cryptoCode: String,
        @Param("type") type: OrderType,
        @Param("price") price: BigDecimal
    ): List<Order>*/

    @Query("SELECT o FROM Order o WHERE o.crypto.code = :cryptoCode AND o.type = :type AND o.price <= :price AND o.status = :status")
    fun findMatchingOrders(
        @Param("cryptoCode") cryptoCode: String,
        @Param("type") type: OrderType,
        @Param("price") price: BigDecimal,
        @Param("status") status: OrderStatus
    ): List<Order>
}