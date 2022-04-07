package com.dansiwiec.customers.controllers

import com.dansiwiec.customers.models.Customer
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

@RestController
@RequestMapping("/customers")
class CustomersController(val mongoTemplate: MongoTemplate) {

    var logger = LoggerFactory.getLogger(this::class.java)!!

    companion object {
        val customers = setOf(
            Customer(id = "1", name = "John Doe", accountId = "123", email = "john.doe@gmail.com"),
            Customer(id = "2", name = "Jane Smith", accountId = "345", email = "jane.smith@yahoo.com"),
            Customer(id = "3", name = "Mark Novak", accountId = "678", email = "mark@novak.com"),
            Customer(id = "4", name = "Ana Mendez", accountId = "901", email = "ana.medndez@gmail.com"),
        )
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: String): Customer? {
        return mongoTemplate.findOne(query(where("id").`is`(id)), Customer::class.java) ?: throw CustomerNotFoundException()
    }

    @PostConstruct
    fun createCustomers() {
        logger.info("Creating customers")
        customers.forEach { mongoTemplate.save(it) }
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "SKU invalid")
class CustomerNotFoundException : RuntimeException()
