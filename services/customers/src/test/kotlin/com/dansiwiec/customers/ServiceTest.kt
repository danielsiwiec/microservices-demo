package com.dansiwiec.customers

import com.dansiwiec.customers.models.Customer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceTest : MongoTestBase() {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun shouldBe200ForAvailableSku() {
        val response = testRestTemplate.getForEntity("/customers/1", Customer::class.java)
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }

    @Test
    fun shouldBe404ForNonExistingSku() {
        val response = testRestTemplate.getForEntity("/customers/999", Customer::class.java)
        assertThat(response.statusCode, equalTo(HttpStatus.NOT_FOUND))
    }
}
