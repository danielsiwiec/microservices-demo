package com.dansiwiec.customers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@ContextConfiguration(initializers = [MongoTestBase.Companion.TestContainerInitializer::class])
abstract class MongoTestBase {

    companion object {
        @Container
        var mongo = MongoDBContainer(DockerImageName.parse("mongo:5-focal"))

        class TestContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.data.mongodb.uri=${mongo.replicaSetUrl}"
                )
            }
        }
    }
}
