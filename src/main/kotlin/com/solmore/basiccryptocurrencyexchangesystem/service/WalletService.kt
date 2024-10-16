package com.solmore.basiccryptocurrencyexchangesystem.service


import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.ResourceNotFound
import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import com.solmore.basiccryptocurrencyexchangesystem.repository.CryptoCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.FiatCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.UserRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.WalletRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
    private val cryptoCurrencyRepository: CryptoCurrencyRepository,
    private val fiatCurrencyRepository: FiatCurrencyRepository
) {

    @Transactional
    fun createWallet(userId: Long, currency: String?): Wallet {
        val user = userRepository.findById(userId)
            .orElseThrow { throw ResourceNotFound("User with ID $userId not found") }

        val cryptoCurrency = currency?.let { cryptoCurrencyRepository.findByCode(it) }
        val fiatCurrency = currency?.let { fiatCurrencyRepository.findByCode(it) }
        val wallet = Wallet(
            user = user,
            currency = cryptoCurrency?.code ?: fiatCurrency!!.code,
            balance = BigDecimal.ZERO
        )

        return walletRepository.save(wallet)
    }

    @Transactional
    fun createWalletsForUser(userId: Long) {
        val cryptocurrencies = cryptoCurrencyRepository.findAll()
        val fiatCurrencies = fiatCurrencyRepository.findAll()

        for (crypto in cryptocurrencies) {
            createWallet(userId, crypto.code)
        }
        for (fiat in fiatCurrencies) {
            createWallet(userId, fiat.code)
        }
    }

    fun getWalletByUserIdAndCurrency(userId: Long, currencyCode: String): Wallet {
        return walletRepository.findByUserIdAndCurrency(userId, currencyCode)
            ?: throw ResourceNotFound("Wallet not found for user $userId with currency $currencyCode")
    }

    fun getWalletsByUserId(userId: Long): List<Wallet> {
        return walletRepository.findAllByUserId(userId)
    }


    @Transactional
    //TODO:оценить транзакционность этой операции
    fun updateWalletBalance(walletId: Long, amount: BigDecimal): Wallet {
        val wallet = walletRepository.findById(walletId)
            .orElseThrow { throw ResourceNotFound("Wallet with ID $walletId not found") }
        wallet.balance += amount
        return walletRepository.save(wallet)
    }
}