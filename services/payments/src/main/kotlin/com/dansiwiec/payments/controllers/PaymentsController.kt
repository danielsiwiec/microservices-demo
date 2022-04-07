package com.dansiwiec.payments.controllers

import com.dansiwiec.payments.models.Order
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
class PaymentsController {

    var logger = LoggerFactory.getLogger(this::class.java)!!
    var paymentsCount = 0

    @PostMapping
    fun sendPayment(@RequestBody order: Order) {
        logger.info("Sending payment for order ${order.id}")
        paymentsCount++
    }
}
