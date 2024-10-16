package com.solmore.basiccryptocurrencyexchangesystem.config

import com.solmore.basiccryptocurrencyexchangesystem.repository.CryptoCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.FiatCurrencyRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.UserRepository
import com.solmore.basiccryptocurrencyexchangesystem.repository.WalletRepository
import com.solmore.basiccryptocurrencyexchangesystem.service.UserService
import com.solmore.basiccryptocurrencyexchangesystem.service.WalletService
import com.solmore.basiccryptocurrencyexchangesystem.web.security.JwtUserDetailsService
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder


@TestConfiguration
@Profile("test")
class TestConfig {

    @Bean
    fun userRepository(): UserRepository = Mockito.mock(UserRepository::class.java)

    @Bean
    fun walletRepository(): WalletRepository = Mockito.mock(WalletRepository::class.java)

    @Bean
    fun cryptoCurrencyRepository(): CryptoCurrencyRepository = Mockito.mock(CryptoCurrencyRepository::class.java)

    @Bean
    fun fiatCurrencyRepository(): FiatCurrencyRepository = Mockito.mock(FiatCurrencyRepository::class.java)


    @Bean
    fun testAuthenticationManager(): AuthenticationManager {
        return Mockito.mock(AuthenticationManager::class.java)
    }


    @Bean
    @Primary
    fun jwtDecoder(): JwtDecoder = Mockito.mock(JwtDecoder::class.java)

    @Bean
    @Primary
    fun jwtEncoder(): JwtEncoder = Mockito.mock(JwtEncoder::class.java)

    @Bean
    fun jwtUserDetailsService(): JwtUserDetailsService {
        return Mockito.mock(JwtUserDetailsService::class.java)
    }

    @Bean
    fun userService(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder,
        authenticationManager: AuthenticationManager,
        jwtEncoder: JwtEncoder,
        jwtDecoder: JwtDecoder,
        jwtUserDetailsService: UserDetailsService
    ): UserService {
        return UserService(
            userRepository,
            passwordEncoder,
            authenticationManager,
            jwtEncoder,
            jwtDecoder,
            jwtUserDetailsService
        )
    }

    @Bean
    fun walletService(
        walletRepository: WalletRepository,
        userRepository: UserRepository,
        cryptoCurrencyRepository: CryptoCurrencyRepository,
        fiatCurrencyRepository: FiatCurrencyRepository
    ): WalletService {
        return WalletService(
            walletRepository,
            userRepository,
            cryptoCurrencyRepository,
            fiatCurrencyRepository
        )
    }
}