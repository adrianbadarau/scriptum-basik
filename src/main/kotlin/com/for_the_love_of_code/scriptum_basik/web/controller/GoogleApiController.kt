package com.for_the_love_of_code.scriptum_basik.web.controller

import com.for_the_love_of_code.scriptum_basik.service.google.DocsService
import com.for_the_love_of_code.scriptum_basik.service.google.DriveService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/google/drive")
class GoogleApiController(
    private val googleDriveService: DriveService,
    private val googleDocsService: DocsService
) {

    private val logger = LoggerFactory.getLogger(GoogleApiController::class.java)

    @GetMapping("/sign-in")
    fun signIn(response: HttpServletResponse) {
        logger.debug("SSO called")
        response.sendRedirect(googleDriveService.authenticate())
    }

    @GetMapping("/oauth/callback")
    fun saveAuthorisationCode(request: HttpServletRequest) {
        val code = request.getParameter("code")
        googleDriveService.exchangeCodeForTokens(code)
    }

    @GetMapping("/logout")
    fun removeUserSession(request: HttpServletRequest) {
        googleDriveService.logOut(request)
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile):String {
        val fileId = googleDriveService.uploadFile(file)
        val documentText = googleDocsService.readDocumentById(fileId)
        return documentText
    }
}
