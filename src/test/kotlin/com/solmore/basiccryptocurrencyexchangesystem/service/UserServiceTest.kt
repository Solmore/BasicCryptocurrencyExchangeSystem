package com.solmore.basiccryptocurrencyexchangesystem.service

import com.solmore.basiccryptocurrencyexchangesystem.config.TestConfig
import com.solmore.basiccryptocurrencyexchangesystem.domain.user.User
import com.solmore.basiccryptocurrencyexchangesystem.repository.UserRepository
import com.solmore.basiccryptocurrencyexchangesystem.web.security.JwtUserDetailsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
//TODO: Переделать тесты
class UserServiceTest {

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    @Qualifier("testAuthenticationManager")
    private lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var jwtEncoder: JwtEncoder

    @MockBean
    private lateinit var jwtDecoder: JwtDecoder

    @MockBean
    private lateinit var jwtUserDetailsService: JwtUserDetailsService

    @Autowired
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        `when`(passwordEncoder.encode(anyString())).thenReturn("encodedPassword")
    }

    @Test
    fun `createUser should create a new user when username does not exist`() {
        `when`(userRepository.findByUsername(anyString())).thenReturn(null)

        userService.createUser("testuser", "password123")

        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `createUser should throw exception when username already exists`() {
        `when`(userRepository.findByUsername(anyString())).thenReturn(User(username = "testuser", password = "password123"))

        assertThrows(Exception::class.java) {
            userService.createUser("testuser", "password123")
        }

        verify(userRepository, never()).save(any(User::class.java))
    }


    @Test
    fun `refresh should throw IllegalArgumentException for invalid token type`() {
        val jwt = mock(Jwt::class.java)
        `when`(jwt.claims).thenReturn(mapOf("type" to "access"))
        `when`(jwtDecoder.decode(anyString())).thenReturn(jwt)

        assertThrows(IllegalArgumentException::class.java) {
            userService.refresh("invalidRefreshToken")
        }
    }
}