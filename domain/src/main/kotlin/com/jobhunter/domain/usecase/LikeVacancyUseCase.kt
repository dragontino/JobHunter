package com.jobhunter.domain.usecase

import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LikeVacancyUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun likeVacancyById(vacancyId: String): Result<Unit> {
        return withContext(dispatcher) {
            repository.likeVacancyById(vacancyId)
        }
    }
}