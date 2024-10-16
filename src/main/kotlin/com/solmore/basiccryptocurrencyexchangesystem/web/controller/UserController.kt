package com.solmore.basiccryptocurrencyexchangesystem.web.controller

import com.solmore.basiccryptocurrencyexchangesystem.domain.wallet.Wallet
import com.solmore.basiccryptocurrencyexchangesystem.service.UserService
import com.solmore.basiccryptocurrencyexchangesystem.service.WalletService
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.TokenDto
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.UserDto
import com.solmore.basiccryptocurrencyexchangesystem.web.dto.WalletDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val walletService: WalletService,
    private val userService: UserService,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    fun registerUser(@RequestBody userDto: UserDto) {
        userService.createUser(userDto.username, userDto.password)
        userService.getByUsername(userDto.username)?.let { walletService.createWalletsForUser(it.id) }
    }

//    @PostMapping("/auth")
//    @ResponseStatus(HttpStatus.OK)
//    fun auth(@RequestBody userDto: UserDto): TokenDto{
//        val response = userService.authenticate(userDto.username, userDto.password)
//        return response
//    }
//
//    @PostMapping("/refresh")
//    @ResponseStatus(HttpStatus.OK)
//    fun refresh(@RequestBody token:String): TokenDto{
//        val response = userService.refresh(token)
//        return response
//    }

    @PostMapping("/{userId}/wallets")
    fun createWallet(
        @PathVariable userId: Long,
        @RequestParam currency: String,
    ): ResponseEntity<WalletDto> {
        val wallet = walletService.createWallet(userId, currency)
        return ResponseEntity.ok(wallet.toDto())
    }

    @GetMapping("/{userId}/wallets")
    fun getWallets(@PathVariable userId: Long): ResponseEntity<List<WalletDto>> {
        val wallets = walletService.getWalletsByUserId(userId)
        return ResponseEntity.ok(wallets.map { it.toDto() })
    }

    fun Wallet.toDto(): WalletDto {
        return WalletDto(
            id = this.id,
            userId = this.user.id,
            currency = currency,
            balance = this.balance
        )

    }
}