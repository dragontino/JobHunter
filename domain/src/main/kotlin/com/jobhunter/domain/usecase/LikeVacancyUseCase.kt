package com.jobhunter.domain.usecase

import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LikeVacancyUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun likeVacancy(vacancy: Vacancy): Result<Unit> {
        return withContext(dispatcher) {
            repository.likeVacancy(vacancy)
        }
    }

    suspend fun dislikeVacancy(vacancy: Vacancy): Result<Unit> {
        return withContext(dispatcher) {
            repository.dislikeVacancy(vacancy)
        }
    }
}