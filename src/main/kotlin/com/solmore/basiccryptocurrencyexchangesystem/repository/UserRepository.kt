package com.solmore.basiccryptocurrencyexchangesystem.repository

import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String?): User?

    //fun findByUserId(userId: Long?): User?
}