package com.for_the_love_of_code.scriptum_basik.service.google

import com.for_the_love_of_code.scriptum_basik.config.ApplicationProperties
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.DriveScopes
import org.springframework.boot.system.ApplicationHome
import java.io.File
import java.io.InputStreamReader
import javax.annotation.PostConstruct

abstract class GoogleApiService(
    private val applicationProperties: ApplicationProperties
) {
    companion object {
        val HTTP_TRANSPORT = NetHttpTransport()
        val SCOPES = listOf(DriveScopes.DRIVE)
        val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    }

    protected lateinit var flow: GoogleAuthorizationCodeFlow
    protected lateinit var dataStoreFactory: FileDataStoreFactory
    protected val userId = "DUMMY_USER"

    @PostConstruct
    fun postConstruct() {
        val secrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(applicationProperties.google.secretKey.inputStream))
        dataStoreFactory = FileDataStoreFactory(File("${ApplicationHome().dir.absolutePath}/credentials"))
        flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
            .setDataStoreFactory(dataStoreFactory).build()
    }

    fun getCredentials(): Credential? {
        return flow.loadCredential(userId)
    }
}
