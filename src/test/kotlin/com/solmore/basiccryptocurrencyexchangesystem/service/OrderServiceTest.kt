package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.config.TestConfig
import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.CryptoCurrency
import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.FiatCurrency
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.ResourceNotFound
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.Order
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderStatus
import com.solmore.basiccryptocurrencyexchangesystem.domain.order.OrderType
import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import com.solmore.basiccryptocurrencyexchangesystem.repository.CryptoCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.FiatCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.OrderRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@SpringBootTest
//TODO: Переделать тесты
class OrderServiceTest {

    @Autowired
    private lateinit var orderService: OrderService

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var walletService: WalletService

    @MockBean
    private lateinit var orderRepository: OrderRepository

    @MockBean
    private lateinit var orderMatchingService: OrderMatchingService

    @MockBean
    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    @MockBean
    private lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    private val userId = 1L
    private val cryptoCode = "BTC"
    private val fiatCode = "USD"
    private val amount = BigDecimal(1.0)
    private val price = BigDecimal(10000.0)

    private val user = User(id = userId, username = "testUser",password = "testPassword")
    private val cryptoCurrency = CryptoCurrency(id = 1L, code = cryptoCode, name=cryptoCode)
    private val fiatCurrency = FiatCurrency(id = 1L, code = fiatCode, name=fiatCode, symbol = "$")
    private val wallet = Wallet(id = 1L,user=user, currency = cryptoCode,balance = BigDecimal(100000.0))
    private val order = Order(
        user = user,
        crypto = cryptoCurrency,
        fiat = fiatCurrency,
        type = OrderType.BUY,
        amount = amount,
        price = price,
        status = OrderStatus.OPEN
    )

    @BeforeEach
    fun setUp() {
        `when`(userService.getById(userId)).thenReturn(user)
        `when`(cryptoCurrencyRepository.findByCode(cryptoCode)).thenReturn(cryptoCurrency)
        `when`(fiatCurrencyRepository.findByCode(fiatCode)).thenReturn(fiatCurrency)
    }

    @Test
    fun `createOrder should create an order when all conditions are met`() {
        `when`(walletService.getWalletByUserIdAndCurrency(userId, fiatCode)).thenReturn(wallet)
        `when`(orderRepository.save(any(Order::class.java))).thenReturn(order)

        val createdOrder = orderService.createOrder(userId, cryptoCode, fiatCode, OrderType.BUY, amount, price)

        assertNotNull(createdOrder)
        assertEquals(user, createdOrder.user)
        assertEquals(cryptoCurrency, createdOrder.crypto)
        assertEquals(fiatCurrency, createdOrder.fiat)
        assertEquals(OrderType.BUY, createdOrder.type)
        assertEquals(amount, createdOrder.amount)
        assertEquals(price, createdOrder.price)
        assertEquals(OrderStatus.OPEN, createdOrder.status)
        verify(orderMatchingService, times(1)).matchOrders(createdOrder)
    }

    @Test
    fun `createOrder should throw ResourceNotFound when user is not found`() {
        `when`(userService.getById(userId)).thenReturn(null)

        assertThrows<ResourceNotFound> {
            orderService.createOrder(userId, cryptoCode, fiatCode, OrderType.BUY, amount, price)
        }
    }

    @Test
    fun `createOrder should throw ResourceNotFound when crypto currency is not found`() {
        `when`(cryptoCurrencyRepository.findByCode(cryptoCode)).thenReturn(null)

        assertThrows<ResourceNotFound> {
            orderService.createOrder(userId, cryptoCode, fiatCode, OrderType.BUY, amount, price)
        }
    }

    @Test
    fun `createOrder should throw ResourceNotFound when fiat currency is not found`() {
        `when`(fiatCurrencyRepository.findByCode(fiatCode)).thenReturn(null)

        assertThrows<ResourceNotFound> {
            orderService.createOrder(userId, cryptoCode, fiatCode, OrderType.BUY, amount, price)
        }
    }

    @Test
    fun `createOrder should throw InsufficientFundsException when wallet balance is insufficient`() {
        `when`(walletService.getWalletByUserIdAndCurrency(userId, fiatCode)).thenReturn(Wallet(user=user,balance = BigDecimal(0.0),currency = "USD"))

        assertThrows<InsufficientFundsException> {
            orderService.createOrder(userId, cryptoCode, fiatCode, OrderType.BUY, amount, price)
        }
    }

}