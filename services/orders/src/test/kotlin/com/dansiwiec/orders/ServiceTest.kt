package com.dansiwiec.orders

import com.dansiwiec.orders.models.LineItem
import com.dansiwiec.orders.models.Order
import com.dansiwiec.orders.models.OrderRequest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.web.client.ExpectedCount.manyTimes
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ServiceTest : MongoTestBase() {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${skuServiceUrl}")
    lateinit var skuServiceUrl: String

    @Value("\${customerServiceUrl}")
    lateinit var customerServiceUrl: String

    @BeforeEach
    fun init() {
        val mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build()
        stubSkus(mockServer, listOf(1, 2, 3))
        stubCustomers(mockServer, listOf(1))
    }

    private fun stubCustomers(mockServer: MockRestServiceServer, customers: List<Int>) {
        mockServer
            .expect(manyTimes(), requestTo(matchesPattern("$customerServiceUrl/customers/[${customers.joinToString("")}]")))
            .andRespond(withStatus(HttpStatus.OK))
        mockServer.expect(
            manyTimes(),
            requestTo(matchesPattern("$customerServiceUrl/customers/[^${customers.joinToString("")}]"))
        )
            .andRespond(withStatus(HttpStatus.NOT_FOUND))
    }

    private fun stubSkus(mockServer: MockRestServiceServer, skus: List<Int>) {
        mockServer
            .expect(manyTimes(), requestTo(matchesPattern("$skuServiceUrl/skus/[${skus.joinToString("")}]")))
            .andRespond(withStatus(HttpStatus.OK))
        mockServer.expect(manyTimes(), requestTo(matchesPattern("$skuServiceUrl/skus/[^${skus.joinToString("")}]")))
            .andRespond(withStatus(HttpStatus.NOT_FOUND))
    }

    @Test
    fun testCreateOrderHappyPath() {
        val response = testRestTemplate.postForEntity(
            "/orders",
            OrderRequest(items = listOf(LineItem("1", 1), LineItem("3", 2)), customerId = "1"),
            Order::class.java
        )
        val orderId = response.body!!.id

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(orderId, notNullValue())

        val orderGetResponse = testRestTemplate.getForEntity("/orders/$orderId", Order::class.java)
        assertThat(orderGetResponse.statusCode, equalTo(HttpStatus.OK))
        assertThat(orderGetResponse.body?.items?.size, equalTo(2))
    }

    @Test
    fun testCreateOrderSkuNotFound() {
        val response = testRestTemplate.postForEntity(
            "/orders",
            OrderRequest(items = listOf(LineItem("1", 1), LineItem("5", 2)), customerId = "1"),
            Order::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }

    @Test
    fun testCreateOrderCustomerNotFound() {
        val response = testRestTemplate.postForEntity(
            "/orders",
            OrderRequest(items = listOf(LineItem("1", 1), LineItem("3", 2)), customerId = "2"),
            Order::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }

    @Test
    fun testCreateOrderNoLineItems() {
        val response = testRestTemplate.postForEntity(
            "/orders", OrderRequest(items = emptyList(), customerId = "1"), Order::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }

    @Test
    fun testCreateOrderNoCustomer() {
        val response = testRestTemplate.postForEntity(
            "/orders",
            OrderRequest(items = listOf(LineItem("1", 1), LineItem("3", 2)), customerId = ""),
            Order::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }
}
