package com.dansiwiec.orders.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate? {
        return restTemplateBuilder
            .errorHandler(errorHandler())
            .build()
    }

    fun errorHandler(): DefaultResponseErrorHandler {
        return object : DefaultResponseErrorHandler() {
            override fun hasError(response: ClientHttpResponse): Boolean {
                return false
            }
        }
    }
}
