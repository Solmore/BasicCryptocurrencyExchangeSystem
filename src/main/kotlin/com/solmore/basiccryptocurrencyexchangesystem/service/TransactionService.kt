package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.Transaction
import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.TransactionType
import com.solmore.basiccryptocurrencyexchangesystem.repository.TransactionRepository
import jakarta.transaction.InvalidTransactionException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val walletService: WalletService,
    private val transactionRepository: TransactionRepository
) {

    @Transactional
    fun deposit(userId: Long, currencyCode: String, amount: BigDecimal): Transaction {
        if (amount <= BigDecimal.ZERO) {
            throw InvalidTransactionException("Amount must be positive")
        }

        val wallet = walletService.getWalletByUserIdAndCurrency(userId, currencyCode)

        walletService.updateWalletBalance(wallet.id, amount)

        val transaction = Transaction(
            wallet = wallet,
            user = wallet.user,
            type = TransactionType.DEPOSIT,
            amount = amount
        )
        return transactionRepository.save(transaction)
    }

    @Transactional
    fun withdraw(userId: Long, currencyCode: String, amount: BigDecimal): Transaction {
        if (amount <= BigDecimal.ZERO) {
            throw InvalidTransactionException("Amount must be positive")
        }

        val wallet = walletService.getWalletByUserIdAndCurrency(userId, currencyCode)


        if (wallet.balance < amount) {
            throw InsufficientFundsException("Insufficient balance in the wallet")
        }

        walletService.updateWalletBalance(wallet.id, amount.negate())

        val transaction = Transaction(
            wallet = wallet,
            user = wallet.user,
            type = TransactionType.WITHDRAWAL,
            amount = amount
        )
        return transactionRepository.save(transaction)
    }
}