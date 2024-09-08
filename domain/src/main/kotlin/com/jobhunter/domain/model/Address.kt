package com.jobhunter.domain.model

data class Address(
    val town: String,
    val street: String,
    val house: String,
) {
    override fun toString(): String = "$town, $street, $house"
}
