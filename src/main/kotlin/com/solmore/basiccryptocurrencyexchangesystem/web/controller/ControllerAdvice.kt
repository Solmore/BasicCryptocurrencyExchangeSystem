package com.solmore.basiccryptocurrencyexchangesystem.web.controller

import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.ExceptionBody
import com.solmore.basiccryptocurrencyexchangesystem.domain.exception.InsufficientFundsException
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Slf4j
class ControllerAdvice {

    private val logger = LoggerFactory.getLogger(ControllerAdvice::class.java)

    @ExceptionHandler(InsufficientFundsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInsufficientFundsException(e: InsufficientFundsException): ExceptionBody {
        logger.error("Insufficient funds error: {}", e.message)
        return ExceptionBody(e.message ?: "Insufficient funds.")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ExceptionBody {
        logger.error("Exception = {}", e.message)
        return ExceptionBody("Internal error.")
    }
}