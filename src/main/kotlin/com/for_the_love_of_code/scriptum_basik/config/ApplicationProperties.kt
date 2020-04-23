package com.for_the_love_of_code.scriptum_basik.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

/**
 * Properties specific to Scriptum Basik.
 *
 * Properties are configured in the `application.yml` file.
 * See [io.github.jhipster.config.JHipsterProperties] for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties {
    var google = Google()

    class Google {
        lateinit var callbackUri: String
        lateinit var secretKey: Resource
        lateinit var credentialsFolder: Resource
    }
}
