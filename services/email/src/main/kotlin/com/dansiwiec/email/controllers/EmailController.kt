package com.dansiwiec.email.controllers

import com.dansiwiec.email.models.Order
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/emails")
class EmailController {

    var logger = LoggerFactory.getLogger(this::class.java)!!
    var emailCount = 0

    @PostMapping("/orderConfirmation")
    fun sendOrderConfirmation(@RequestBody order: Order) {
        logger.info("Sending an email confirming order ${order.id}")
        emailCount++
    }
}
