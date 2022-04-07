package com.dansiwiec.email

import com.dansiwiec.email.models.LineItem
import com.dansiwiec.email.models.Order
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
    fun testSendEmailWhenOrderCreated() {
        val order = Order(1, listOf(LineItem("1", 2)))
        val response = testRestTemplate.postForEntity("/emails/orderConfirmation", order, Any::class.java)

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val emailCount: Int? = testRestTemplate.getForObject("/emails/sentEmailCount")
        assertThat(emailCount, equalTo(1))
    }
}
