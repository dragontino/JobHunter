package com.jobhunter.domain.usecase

import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllVacanciesUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    fun getAllVacancies(): Flow<List<Vacancy>> {
        return repository.getAllVacancies().flowOn(dispatcher)
    }
}