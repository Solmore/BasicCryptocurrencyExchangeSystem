package com.solmore.basiccryptocurrencyexchangesystem.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


class Config {

    @Configuration
    class CorsConfig : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**") // Разрешаем все пути
                .allowedOrigins("*") // Разрешаем все источники (домены)
                .allowedMethods("*") // Разрешаем все методы (GET, POST, PUT, DELETE и т.д.)
                .allowedHeaders("*") // Разрешаем все заголовки
        }
    }
}