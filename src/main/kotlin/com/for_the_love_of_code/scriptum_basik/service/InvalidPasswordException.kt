package com.for_the_love_of_code.scriptum_basik.service

class InvalidPasswordException : RuntimeException("Incorrect password") {
    companion object {
        private const val serialVersionUID = 1L
    }
}
