package com.dansiwiec.email.controllers

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/emails")
@Profile("test")
class EmailInspector(val emailController: EmailController) {

    @GetMapping("/sentEmailCount")
    fun count(): Int {
        return emailController.emailCount
    }
}
