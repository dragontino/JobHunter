package com.jobhunter.domain.usecase

import android.util.Log
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class GetVacancyUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    private companion object {
        const val TAG = "GetVacancyUseCase"
    }

    fun getVacancyById(id: String): Flow<Vacancy> =
        repository.getVacancyById(id)
            .flowOn(dispatcher)
            .catch { Log.e(TAG, it.message, it) }
}