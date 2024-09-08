package com.jobhunter.domain.model

data class Offer(
    val id: OfferId?,
    val title: String,
    val link: String,
    val button: Button?
)
