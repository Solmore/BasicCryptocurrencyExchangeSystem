package com.solmore.basiccryptocurrencyexchangesystem.web.security

import com.solmore.basiccryptocurrencyexchangesystem.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(@Lazy private val userService: UserService) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userService.getByUsername(username)
        ?: throw UsernameNotFoundException("User not found")
        return User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.roles)
            .build()
    }
}