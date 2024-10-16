package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.config.TestConfig
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.Transaction
import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.TransactionType
import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import com.solmore.basiccryptocurrencyexchangesystem.repository.TransactionRepository
import jakarta.transaction.InvalidTransactionException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
//TODO: Переделать тесты
class TransactionServiceTest {

    @Autowired
    private lateinit var transactionService: TransactionService

    @MockBean
    private lateinit var walletService: WalletService

    @MockBean
    private lateinit var transactionRepository: TransactionRepository

    private val userId = 1L
    private val currencyCode = "USD"
    private val amount = BigDecimal(100.0)
    private val wallet = Wallet(id = 1L, balance = BigDecimal(500.0), user = User(id = userId, username = "testUser", password = "testPassword", roles = "USER"),currency = currencyCode)
    private val transaction = Transaction(
        wallet = wallet,
        user = wallet.user,
        type = TransactionType.DEPOSIT,
        amount = amount
    )

    @BeforeEach
    fun setUp() {
        `when`(walletService.getWalletByUserIdAndCurrency(userId, currencyCode)).thenReturn(wallet)
        `when`(transactionRepository.save(any(Transaction::class.java))).thenReturn(transaction)
    }


    @Test
    fun `deposit should throw InvalidTransactionException when amount is zero or negative`() {
        assertThrows<InvalidTransactionException> {
            transactionService.deposit(userId, currencyCode, BigDecimal.ZERO)
        }

        assertThrows<InvalidTransactionException> {
            transactionService.deposit(userId, currencyCode, BigDecimal(-100.0))
        }
    }


    @Test
    fun `withdraw should throw InvalidTransactionException when amount is zero or negative`() {
        assertThrows<InvalidTransactionException> {
            transactionService.withdraw(userId, currencyCode, BigDecimal.ZERO)
        }

        assertThrows<InvalidTransactionException> {
            transactionService.withdraw(userId, currencyCode, BigDecimal(-100.0))
        }
    }

    @Test
    fun `withdraw should throw InsufficientFundsException when wallet balance is insufficient`() {
        `when`(walletService.getWalletByUserIdAndCurrency(userId, currencyCode)).thenReturn(wallet.copy(balance = BigDecimal(50.0)))

        assertThrows<InsufficientFundsException> {
            transactionService.withdraw(userId, currencyCode, amount)
        }
    }
}