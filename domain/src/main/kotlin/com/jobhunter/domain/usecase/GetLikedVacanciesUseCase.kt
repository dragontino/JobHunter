package com.jobhunter.domain.usecase

import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetLikedVacanciesUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    fun getLikedVacancies(): Flow<List<Vacancy>> {
        return repository.getLikedVacancies().flowOn(dispatcher)
    }
}