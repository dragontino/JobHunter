package com.jobhunter.domain.model

import java.time.LocalDate

data class Vacancy(
    val id: String,
    val title: String,
    val description: String?,
    val lookingNumber: Int?,
    val appliedNumber: Int?,
    val company: String,
    val address: Address,
    val experience: Experience,
    val publishedDate: LocalDate,
    val isFavorite: Boolean,
    val salary: Salary,
    val schedules: List<String>,
    val responsibilities: String,
    val questions: List<String>
)