package com.for_the_love_of_code.scriptum_basik.config

import com.for_the_love_of_code.scriptum_basik.domain.User
import com.for_the_love_of_code.scriptum_basik.service.MailService
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NoOpMailConfiguration {
    private val mockMailService = Mockito.mock(MailService::class.java)

    @Bean
    fun mailService(): MailService {
        return mockMailService
    }

    init {
        Mockito.doNothing().`when`(mockMailService).sendActivationEmail(User())
    }
}
