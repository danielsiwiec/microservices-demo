package com.dansiwiec.orders.services

import com.dansiwiec.orders.models.Customer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CustomerService(val restTemplate: RestTemplate) {

    val logger = LoggerFactory.getLogger(this::class.java)!!

    @Value("\${customerServiceUrl}")
    private lateinit var customerServiceUrl: String

    fun isValidCustomer(id: String): Boolean {
        return restTemplate.getForEntity(
            "$customerServiceUrl/customers/$id",
            Customer::class.java
        ).statusCode.is2xxSuccessful
    }
}
