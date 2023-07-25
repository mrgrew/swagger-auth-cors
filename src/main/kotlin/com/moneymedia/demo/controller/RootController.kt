package com.moneymedia.demo.controller

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api", produces = [MediaType.APPLICATION_JSON_VALUE])
class RootController {

    @ApiResponses(
        ApiResponse(responseCode = "200"), ApiResponse(responseCode = "401"), ApiResponse(responseCode = "403")
    )
    @GetMapping
    fun root() = "{}"
}
