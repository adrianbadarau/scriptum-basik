package com.for_the_love_of_code.scriptum_basik.service

class UsernameAlreadyUsedException : RuntimeException("Login name already used!") {
    companion object {
        private const val serialVersionUID = 1L
    }
}
