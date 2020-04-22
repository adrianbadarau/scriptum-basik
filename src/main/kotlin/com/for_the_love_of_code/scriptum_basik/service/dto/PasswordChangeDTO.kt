package com.for_the_love_of_code.scriptum_basik.service.dto

/**
 * A DTO representing a password change required data - current and new password.
 */
data class PasswordChangeDTO(var currentPassword: String? = null, var newPassword: String? = null)
