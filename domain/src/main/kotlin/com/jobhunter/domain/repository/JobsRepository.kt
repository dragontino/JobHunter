package com.jobhunter.domain.repository

import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.Vacancy
import kotlinx.coroutines.flow.Flow

interface JobsRepository {
    fun getOffers(): Flow<List<Offer>>

    fun getAllVacancies(): Flow<List<Vacancy>>

    fun getLikedVacancies(): Flow<List<Vacancy>>

    suspend fun likeVacancyById(vacancyId: String): Result<Unit>

    suspend fun getVacancyById(vacancyId: String): Result<Vacancy>
}