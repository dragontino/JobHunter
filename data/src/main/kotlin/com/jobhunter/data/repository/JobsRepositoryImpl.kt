package com.jobhunter.data.repository

import com.jobhunter.data.retrofit.JobsApiService
import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class JobsRepositoryImpl(private val retrofitService: JobsApiService) : JobsRepository {
    override fun getOffers(): Flow<List<Offer>> {
        return flow {
            val offers = retrofitService.getJobApi().offers.map { it.convertToDomainOffer() }
            emit(offers)
        }
    }

    override fun getAllVacancies(): Flow<List<Vacancy>> {
        return flow {
            val vacancies = retrofitService.getJobApi().vacancies.map { it.convertToDomainVacancy() }
            emit(vacancies)
        }
    }

    override fun getLikedVacancies(): Flow<List<Vacancy>> {
        return getAllVacancies().map { vacancies ->
            vacancies.filter { it.isFavorite }
        }
    }

    override suspend fun likeVacancyById(vacancyId: String): Result<Unit> {
        return Result.failure(Exception("This feature is not implemented now"))
    }

    override suspend fun getVacancyById(vacancyId: String): Result<Vacancy> {
        val allVacancies = retrofitService.getJobApi()
            .vacancies
            .map { it.convertToDomainVacancy() }
        return allVacancies
            .find { it.id == vacancyId }
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("Вакансия с id $vacancyId не найдена!"))
    }
}