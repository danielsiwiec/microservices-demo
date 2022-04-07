package com.dansiwiec.orders.models

import org.springframework.data.annotation.Id

data class Order(@field:Id val id: String?, val items: List<LineItem>, val customerId: String) {

    constructor() : this("", emptyList(), "")

    companion object {
        var currenOrderId = 0

        fun toOrder(wireType: OrderRequest): Order {
            return Order(null, wireType.items, wireType.customerId)
        }
    }
}
