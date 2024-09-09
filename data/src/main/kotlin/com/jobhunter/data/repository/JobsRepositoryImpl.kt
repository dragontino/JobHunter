package com.jobhunter.data.repository

import com.jobhunter.data.model.VacancyApi
import com.jobhunter.data.retrofit.JobsApiService
import com.jobhunter.data.room.LikedVacanciesDao
import com.jobhunter.data.room.model.VacancyEntity
import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class JobsRepositoryImpl(
    private val retrofitService: JobsApiService,
    private val likedVacanciesDao: LikedVacanciesDao
) : JobsRepository {
    override fun getOffers(): Flow<List<Offer>> {
        return flow {
            val offers = retrofitService.getJobApi().offers.map { it.convertToDomainOffer() }
            emit(offers)
        }
    }


    private suspend fun getVacanciesApi(): List<VacancyApi> {
        return retrofitService.getJobApi().vacancies
    }

    private fun getLikedVacancyEntities(): Flow<List<VacancyEntity>> {
        return likedVacanciesDao.getLikedVacancies()
    }


    override fun getAllVacancies(): Flow<List<Vacancy>> = flow { emit(getVacanciesApi()) }
        .combine(getLikedVacancyEntities()) { vacanciesApi, likedVacancyEntities ->
            val likedVacanciesIds = likedVacancyEntities.map { it.id }.toSet()
            likedVacancyEntities.map { it.convertToDomainVacancy() } +
                    vacanciesApi
                        .filter { it.id !in likedVacanciesIds }
                        .map { it.convertToDomainVacancy() }
        }


    override fun getLikedVacancies(): Flow<List<Vacancy>> {
        return getAllVacancies().map { it.filter(Vacancy::isFavorite) }
    }

    override suspend fun likeVacancy(vacancy: Vacancy): Result<Unit> {
        if (isVacancyExists(vacancy.id)) {
            val result = likedVacanciesDao.likeVacancy(vacancy.id)
            return when (result) {
                0 -> Result.failure(Exception("Не удалось добавить в избранное вакансию ${vacancy.title}"))
                else -> Result.success(Unit)
            }
        }
        else {
            val vacancyEntity = VacancyEntity(vacancy).copy(isFavorite = true)
            likedVacanciesDao.addVacancy(vacancyEntity)
            return Result.success(Unit)
        }
    }

    override suspend fun dislikeVacancy(vacancy: Vacancy): Result<Unit> {
        if (isVacancyExists(vacancy.id)) {
            val result = likedVacanciesDao.dislikeVacancy(vacancy.id)
            return when (result) {
                0 -> Result.failure(Exception("Не удалось удалить из избранного вакансию ${vacancy.title}"))
                else -> Result.success(Unit)
            }
        }
        else {
            val vacancyEntity = VacancyEntity(vacancy).copy(isFavorite = false)
            likedVacanciesDao.addVacancy(vacancyEntity)
            return Result.success(Unit)
        }
    }

    override fun getVacancyById(vacancyId: String): Flow<Vacancy> {
        val vacancyEntityFlow = likedVacanciesDao.getVacancyById(vacancyId)
        val vacancyApiFlow = flow {
            emit(getVacanciesApi().find { it.id == vacancyId })
        }

        return vacancyEntityFlow.combine(vacancyApiFlow) { vacancyEntity, vacancyApi ->
            vacancyEntity?.convertToDomainVacancy() ?: vacancyApi?.convertToDomainVacancy()
        }.filterNotNull()
    }

    private suspend fun isVacancyExists(vacancyId: String): Boolean {
        return likedVacanciesDao.checkVacancyById(vacancyId) == 1
    }
}