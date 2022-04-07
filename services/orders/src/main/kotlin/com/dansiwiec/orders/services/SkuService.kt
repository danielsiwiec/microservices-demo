package com.dansiwiec.orders.services

import com.dansiwiec.orders.models.Sku
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SkuService(val restTemplate: RestTemplate) {

    @Value("\${skuServiceUrl}")
    private lateinit var skuServiceUrl: String

    fun isValidSku(id: String): Boolean {
        return restTemplate.getForEntity("$skuServiceUrl/skus/$id", Sku::class.java).statusCode.is2xxSuccessful
    }
}
