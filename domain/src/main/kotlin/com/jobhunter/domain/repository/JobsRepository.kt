package com.jobhunter.domain.repository

import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

interface JobsRepository {
    fun getOffers(): Flow<List<Offer>>

    fun getAllVacancies(): Flow<List<Vacancy>>

    fun getLikedVacancies(): Flow<List<Vacancy>>

    suspend fun likeVacancy(vacancy: Vacancy): Result<Unit>

    suspend fun dislikeVacancy(vacancy: Vacancy): Result<Unit>

    fun getVacancyById(vacancyId: String): Flow<Vacancy>
}