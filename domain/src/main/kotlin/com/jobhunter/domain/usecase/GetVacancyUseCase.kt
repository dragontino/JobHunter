package com.jobhunter.domain.usecase

import android.util.Log
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetVacancyUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    private companion object {
        const val TAG = "GetVacancyUseCase"
    }

    suspend fun getVacancyById(id: String): Result<Vacancy> = withContext(dispatcher) {
        repository.getVacancyById(id).onFailure {
            Log.e(TAG, it.message, it)
        }
    }
}