package com.for_the_love_of_code.scriptum_basik.service

class EmailAlreadyUsedException : RuntimeException("Email is already in use!") {
    companion object {
        private const val serialVersionUID = 1L
    }
}
