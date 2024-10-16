package com.solmore.basiccryptocurrencyexchangesystem.config


/*import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


//@Configuration
//@EnableWebSecurity
class ApplicationConfig(
    //private val userDetailsService: JwtUserDetailsService
) {

    @Configuration
    class CorsConfig : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**") // Разрешаем все пути
                .allowedOrigins("*") // Разрешаем все источники (домены)
                .allowedMethods("*") // Разрешаем все методы (GET, POST, PUT, DELETE и т.д.)
                .allowedHeaders("*") // Разрешаем все заголовки
        }
    }
}*/

    /*private val keyPair: KeyPair = Jwks.generateRsaKeyPair()

    @Bean
    fun passwordEncoder(): PasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(configurer: AuthenticationConfiguration): AuthenticationManager {
        return configurer.getAuthenticationManager()
    }




    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("clientId")
            .clientSecret("{bcrypt}clientSecret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8080/callback")
            .scope(OidcScopes.OPENID)
            .scope("read")
            .scope("write")
            .clientSettings(
                ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .requireProofKey(true)
                .build())
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .reuseRefreshTokens(true)
                .build()
            )
            .build()
        return InMemoryRegisteredClientRepository(client)
    }

    //TODO:добавить oauth2 для гугл
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain? {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        return http
            .oauth2ResourceServer { it.jwt { jwt -> jwt.decoder(jwtDecoder()) } }
            .build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .authorizeHttpRequests { configurer ->
                configurer.requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/users/register",
                    "/users/auth",
                    "/users/refresh").permitAll()
                configurer.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt { jwt -> jwt.decoder(jwtDecoder()) } }

        return http.build()
    }


    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk = RSAKey.Builder(keyPair.public as RSAPublicKey)
            .privateKey(keyPair.private as RSAPrivateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
        val jwkSource: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(keyPair.public as RSAPublicKey).build()
    }

}*/