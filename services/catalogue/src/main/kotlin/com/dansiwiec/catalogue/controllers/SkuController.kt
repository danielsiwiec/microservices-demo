package com.dansiwiec.catalogue.controllers

import com.dansiwiec.catalogue.models.Sku
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
@RequestMapping("/skus")
class SkuController(val mongoTemplate: MongoTemplate) {

    var logger = LoggerFactory.getLogger(this::class.java)!!

    companion object {
        val catalogue = setOf(
            Sku(id = "1", name = "Lonely Planet - Vietnam", category = "books", price = 19.99),
            Sku(id = "2", name = "Yamaha Acoustic Guitar", category = "music instruments", price = 220.0),
            Sku(id = "3", name = "Indoor HEPA Air Filter", category = "home appliances", price = 99.0),
            Sku(id = "4", name = "Nintendo Switch", category = "game consoles", price = 299.0),
            Sku(id = "5", name = "Roses bouquet", category = "plants", price = 25.0),
            Sku(id = "6", name = "100% Orange Juice", category = "groceries", price = 3.99),
        )
    }

    @GetMapping("/{id}")
    fun getSku(@PathVariable id: String): Sku? {
        return mongoTemplate.findOne(query(where("id").`is`(id)), Sku::class.java) ?: throw SkuNotFoundException()
    }

    @PostConstruct
    fun createSkus() {
        logger.info("Publishing SKUs")
        catalogue.forEach { mongoTemplate.save(it) }
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "SKU invalid")
class SkuNotFoundException : RuntimeException()
