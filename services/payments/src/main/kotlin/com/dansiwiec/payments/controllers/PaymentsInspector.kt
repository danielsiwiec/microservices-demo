package com.dansiwiec.payments.controllers

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
@Profile("test")
class PaymentsInspector(val paymentsController: PaymentsController) {

    @GetMapping("/count")
    fun count(): Int {
        return paymentsController.paymentsCount
    }
}
