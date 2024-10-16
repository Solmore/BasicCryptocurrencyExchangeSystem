package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import com.solmore.basiccryptocurrencyexchangesystem.repository.UserRepository
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.TokenDto
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
@Slf4j
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val userDetailsService: UserDetailsService
) {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(username: String, password: String) {
        logger.info("Creating user {}", username)
        if(userRepository.findByUsername(username) != null) {
            throw Exception("User already exists")
        }
        val user = User(
            username = username,
            password = passwordEncoder.encode(password)
        )
        userRepository.save(user)
    }

    fun getByUsername(username: String?): User? {
        logger.info("Get user by username: {}", username)
        return userRepository.findByUsername(username)
    }

    fun getById(id: Long): User? {
        logger.info("Get user by id: {}", id)
        return userRepository.findById(id).orElse(null)
    }

    fun authenticate(username: String, password: String): TokenDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )

        if (authentication.isAuthenticated) {
            val principal = authentication.principal as UserDetails
            val accessToken = generateToken(principal.username, "access")
            val refreshToken = generateToken(principal.username, "refresh")

            return TokenDto(accessToken, refreshToken)
        } else {
            throw BadCredentialsException("Authentication failed")
        }
    }

    fun refresh(token: String): TokenDto {

        val decodedJwt = jwtDecoder.decode(token)

        val tokenType = decodedJwt.claims["type"] as String?
        if (tokenType != "refresh") {
            throw IllegalArgumentException("Invalid token type")
        }
        val accessToken = generateToken(decodedJwt.subject, "access")
        val refreshToken = generateToken(decodedJwt.subject, "refresh")

        return TokenDto(accessToken, refreshToken)
    }

    private fun generateToken(username: String, tokenType: String): String {
        val now = Instant.now()
        val expiry = if (tokenType == "access") {
            now.plus(1, ChronoUnit.HOURS)
        } else {
            now.plus(30, ChronoUnit.DAYS)
        }

        val claims = JwtClaimsSet.builder()
            .subject(username)
            .issuedAt(now)
            .expiresAt(expiry)
            .claim("type", tokenType)
            .build()

        val jwt = JwtEncoderParameters.from(claims)
        return jwtEncoder.encode(jwt).tokenValue
    }

}