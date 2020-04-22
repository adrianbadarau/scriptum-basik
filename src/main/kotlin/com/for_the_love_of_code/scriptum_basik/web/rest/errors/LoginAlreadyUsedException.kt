package com.for_the_love_of_code.scriptum_basik.web.rest.errors

import org.zalando.problem.Exceptional

class LoginAlreadyUsedException :
    BadRequestAlertException(LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists") {

    override fun getCause(): Exceptional? = super.cause

    companion object {
        private const val serialVersionUID = 1L
    }
}
