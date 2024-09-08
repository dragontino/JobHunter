package com.jobhunter.data.model

import com.jobhunter.domain.model.Address
import com.jobhunter.domain.model.Button
import com.jobhunter.domain.model.Experience
import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.OfferId
import com.jobhunter.domain.model.Salary
import com.jobhunter.domain.model.Vacancy
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class JobApi(
    val offers: List<OfferApi>,
    val vacancies: List<VacancyApi>
)


data class OfferApi(
    val id: String?,
    val title: String,
    val link: String,
    val button: Button?
) {
    fun convertToDomainOffer() = Offer(
        id = getOfferId(id),
        title = title,
        link = link,
        button = button
    )

    private fun getOfferId(id: String?): OfferId? = when (id) {
        "near_vacancies" -> OfferId.NearVacancies
        "level_up_resume" -> OfferId.LevelUpResume
        "temporary_job" -> OfferId.TemporaryJob
        else -> null
    }
}


data class VacancyApi(
    val id: String,
    val title: String,
    val description: String?,
    val lookingNumber: Int?,
    val appliedNumber: Int?,
    val company: String,
    val address: Address,
    val experience: Experience,
    val isFavorite: Boolean,
    val salary: Salary,
    val publishedDate: String,
    val responsibilities: String,
    val schedules: List<String>,
    val questions: List<String>
) {
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
        publishedDate = publishedDate.toLocalDate(),
        responsibilities = responsibilities,
        schedules = schedules,
        questions = questions
    )

    private fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return LocalDate.parse(this, formatter)
    }

    private companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
    }
}