package com.for_the_love_of_code.scriptum_basik.service.google

import com.for_the_love_of_code.scriptum_basik.config.ApplicationProperties
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.Serializable
import javax.servlet.http.HttpServletRequest

@Service
class DriveService(
    private val applicationProperties: ApplicationProperties
) : GoogleApiService(applicationProperties) {
    private val drive: Drive by lazy {
        Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).setApplicationName("Scriptum-Basik").build()
    }

    fun authenticate(): String {
        val url = flow.newAuthorizationUrl()
        return url.setRedirectUri(applicationProperties.google.callbackUri).setAccessType("offline").build()
    }

    fun exchangeCodeForTokens(code: String) {
        val tokenResponse = flow.newTokenRequest(code).setRedirectUri(applicationProperties.google.callbackUri).execute()
        flow.createAndStoreCredential(tokenResponse, userId)
    }

    fun logOut(request: HttpServletRequest) {
        dataStoreFactory.getDataStore<Serializable>(applicationProperties.google.credentialsFolder.filename).clear()
    }

    fun isAuthenticated(): Boolean {
        getCredentials()?.let {
            return it.refreshToken()
        }
        return false
    }

    fun uploadFile(file: MultipartFile): String {
        val tempFile = File("/temp", file.originalFilename!!)
        if (tempFile.exists()) {
            tempFile.delete()
        }
        file.transferTo(tempFile)
        val content = FileContent(file.contentType, tempFile)
        val toDrive = com.google.api.services.drive.model.File()
        toDrive.name = file.originalFilename
        toDrive.mimeType = "application/vnd.google-apps.document"
        val execute = drive.files().create(toDrive, content).setOcrLanguage("en").setFields("id").execute()
        return execute.id
    }
}
