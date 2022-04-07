package com.dansiwiec.payments

import com.dansiwiec.payments.models.LineItem
import com.dansiwiec.payments.models.Order
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceTest : MongoTestBase() {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun testSendPaymentForOrder() {
        val order = Order(1, listOf(LineItem("1", 2)))
        val response = testRestTemplate.postForEntity("/payments", order, Any::class.java)

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val paymentsCount: Int? = testRestTemplate.getForObject("/payments/count")
        assertThat(paymentsCount, equalTo(1))
    }
}
