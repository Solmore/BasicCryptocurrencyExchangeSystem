package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN t.wallet w WHERE w.user.id = :userId")
    fun findByUserId(@Param("userId") userId: Long): List<Transaction>

    fun findByWalletId(walletId: Long): List<Transaction>
}