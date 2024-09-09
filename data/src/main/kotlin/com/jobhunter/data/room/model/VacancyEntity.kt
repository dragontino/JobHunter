package com.jobhunter.data.room.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jobhunter.data.room.ListConverter
import com.jobhunter.data.room.LocalDateConverter
import com.jobhunter.domain.model.Address
import com.jobhunter.domain.model.Experience
import com.jobhunter.domain.model.Salary
import com.jobhunter.domain.model.Vacancy
import java.time.LocalDate

@TypeConverters(LocalDateConverter::class, ListConverter::class)
@Entity(tableName = "Vacancy")
data class VacancyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val lookingNumber: Int?,
    val appliedNumber: Int?,
    val company: String,
    @Embedded("address_")
    val address: Address,
    @Embedded("experience_")
    val experience: Experience,
    val isFavorite: Boolean,
    @Embedded("salary_")
    val salary: Salary,
    val publishedDate: LocalDate,
    val responsibilities: String,
    val schedules: List<String>,
    val questions: List<String>
) {
    constructor(vacancy: Vacancy) : this(
        id = vacancy.id,
        title = vacancy.title,
        description = vacancy.description,
        lookingNumber = vacancy.lookingNumber,
        appliedNumber = vacancy.appliedNumber,
        company = vacancy.company,
        address = vacancy.address,
        experience = vacancy.experience,
        isFavorite = vacancy.isFavorite,
        salary = vacancy.salary,
        publishedDate = vacancy.publishedDate,
        responsibilities = vacancy.responsibilities,
        schedules = vacancy.schedules,
        questions = vacancy.questions
    )

    fun convertToDomainVacancy() = Vacancy(
        id = id,
        title = title,
        description = description,
        lookingNumber = lookingNumber,
        appliedNumber = appliedNumber,
        company = company,
        address = address,
        experience = experience,
        isFavorite = isFavorite,
        salary = salary,
        publishedDate = publishedDate,
        responsibilities = responsibilities,
        schedules = schedules,
        questions = questions
    )
}