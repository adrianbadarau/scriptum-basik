package com.for_the_love_of_code.scriptum_basik.web.rest.errors

import java.io.Serializable

class FieldErrorVM(val objectName: String, val field: String, val message: String?) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
