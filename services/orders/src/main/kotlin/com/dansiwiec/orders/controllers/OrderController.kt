package com.dansiwiec.orders.controllers

import com.dansiwiec.orders.models.Order
import com.dansiwiec.orders.models.OrderRequest
import com.dansiwiec.orders.services.CustomerService
import com.dansiwiec.orders.services.SkuService
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/orders")
class OrderController(
    val skuService: SkuService,
    val customerService: CustomerService,
    val mongoTemplate: MongoTemplate
) {

    var logger = LoggerFactory.getLogger(this::class.java)!!

    @PostMapping
    fun createOrder(@RequestBody @Valid wireOrder: OrderRequest): ResponseEntity<Order> {
        val order = Order.toOrder(wireOrder)
        validateOrder(order)
        val inserted = mongoTemplate.insert(order)
        logger.info("Created order ${inserted.id}")
        return ResponseEntity.ok(inserted)
    }

    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: Int): Order? {
        return mongoTemplate.findOne(query(where("id").`is`(id)), Order::class.java) ?: throw OrderNotFoundException()
    }

    private fun validateOrder(order: Order) {
        if (order.items.any { !skuService.isValidSku(it.sku) }) throw BadSkuException()
        if (!customerService.isValidCustomer(order.customerId)) throw BadCustomerException()
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "SKU invalid")
class BadSkuException : RuntimeException()

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Customer invalid")
class BadCustomerException : RuntimeException()

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Order not found")
class OrderNotFoundException : RuntimeException()
