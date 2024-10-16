package com.solmore.basiccryptocurrencyexchangesystem.web.security

//@Service
//class JwtUserDetailsService(@Lazy private val userService: UserService) : UserDetailsService {
//
//    override fun loadUserByUsername(username: String?): UserDetails {
//        val user = userService.getByUsername(username)
//        ?: throw UsernameNotFoundException("User not found")
//        return User.builder()
//            .username(user.username)
//            .password(user.password)
//            .roles(user.roles)
//            .build()
//    }
//}