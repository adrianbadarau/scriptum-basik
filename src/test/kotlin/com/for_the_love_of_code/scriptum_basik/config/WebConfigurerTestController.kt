package com.for_the_love_of_code.scriptum_basik.config

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebConfigurerTestController {

    @GetMapping("/api/test-cors")
    fun testCorsOnApiPath() = Unit

    @GetMapping("/test/test-cors")
    fun testCorsOnOtherPath() = Unit
}
