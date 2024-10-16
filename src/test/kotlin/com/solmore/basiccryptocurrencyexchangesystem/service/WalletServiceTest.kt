package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.config.TestConfig
import com.solmore.basiccryptocurrencyexchangesystem.domain.currency.CryptoCurrency
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.ResourceNotFound
import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import com.solmore.basiccryptocurrencyexchangesystem.repository.CryptoCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.FiatCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.UserRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.WalletRepository
import com.solmore.basiccryptocurrencyexchangesystem.web.security.JwtUserDetailsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
//TODO: Переделать тесты
class WalletServiceTest {

    @Autowired
    private lateinit var walletService: WalletService
    @MockBean
    private lateinit var userRepository: UserRepository
    @MockBean
    private lateinit var walletRepository: WalletRepository
    @MockBean
    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository
    @MockBean
    private lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    @Autowired
    @Qualifier("testAuthenticationManager")
    lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var jwtUserDetailsService: JwtUserDetailsService

    @Test
    fun `createWallet should create and save a new wallet for user`() {
        val userId = 1L
        val user = User(id = userId, username = "testuser", password = "password")
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val currencyCode = "BTC"
        `when`(cryptoCurrencyRepository.findByCode(currencyCode)).thenReturn(CryptoCurrency(id = 1L, code = currencyCode, name = "BTC"))
        `when`(walletRepository.save(any(Wallet::class.java))).thenAnswer { it.arguments[0] }

        val wallet = walletService.createWallet(userId, currencyCode)

        assertNotNull(wallet)
        assertEquals(wallet.currency, currencyCode)
        assertEquals(wallet.balance, BigDecimal.ZERO)
        verify(walletRepository, times(1)).save(any(Wallet::class.java))
    }

    @Test
    fun `createWallet should throw ResourceNotFoundException when user is not found`() {
        val userId = 1L
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows(ResourceNotFound::class.java) {
            walletService.createWallet(userId, "BTC")
        }
        verify(walletRepository, never()).save(any(Wallet::class.java))
    }


    @Test
    fun `getWalletByUserIdAndCurrency should return wallet for valid user and currency`() {
        val userId = 1L
        val currencyCode = "BTC"
        val wallet = Wallet(id = 1L, user = User(id = userId, username = "testuser", password = "password"), currency = currencyCode, balance = BigDecimal.ZERO)

        `when`(walletRepository.findByUserIdAndCurrency(userId, currencyCode)).thenReturn(wallet)

        val retrievedWallet = walletService.getWalletByUserIdAndCurrency(userId, currencyCode)

        assertNotNull(retrievedWallet)
        assertEquals(wallet, retrievedWallet)
    }

    @Test
    fun `getWalletByUserIdAndCurrency should throw ResourceNotFoundException when wallet not found`() {
        val userId = 1L
        val currencyCode = "BTC"
        `when`(walletRepository.findByUserIdAndCurrency(userId, currencyCode)).thenReturn(null)

        assertThrows(ResourceNotFound::class.java) {
            walletService.getWalletByUserIdAndCurrency(userId, currencyCode)
        }
    }

    @Test
    fun `getWalletsByUserId should return list of wallets for user`() {
        val userId = 1L
        val wallets = listOf(
            Wallet(id = 1L, user = User(id = userId, username = "testuser", password = "password"), currency = "BTC", balance = BigDecimal.ZERO),
            Wallet(id = 2L, user = User(id = userId, username = "testuser", password = "password"), currency = "USD", balance = BigDecimal.ZERO)
        )
        `when`(walletRepository.findAllByUserId(userId)).thenReturn(wallets)

        val retrievedWallets = walletService.getWalletsByUserId(userId)

        assertNotNull(retrievedWallets)
        assertEquals(2, retrievedWallets.size)
    }

    @Test
    fun `updateWalletBalance should update wallet balance and save`() {
        val walletId = 1L
        val wallet = Wallet(id = walletId, user = User(id = 1L, username = "testuser", password = "password"), currency = "BTC", balance = BigDecimal(100))

        `when`(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet))
        `when`(walletRepository.save(any(Wallet::class.java))).thenAnswer { it.arguments[0] }

        val updatedWallet = walletService.updateWalletBalance(walletId, BigDecimal(50))

        assertNotNull(updatedWallet)
        assertEquals(BigDecimal(150), updatedWallet.balance)
        verify(walletRepository, times(1)).save(wallet)
    }

    @Test
    fun `updateWalletBalance should throw ResourceNotFoundException when wallet not found`() {
        val walletId = 1L
        `when`(walletRepository.findById(walletId)).thenReturn(Optional.empty())

        assertThrows(ResourceNotFound::class.java) {
            walletService.updateWalletBalance(walletId, BigDecimal(50))
        }
        verify(walletRepository, never()).save(any(Wallet::class.java))
    }
}