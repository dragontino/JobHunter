package com.jobhunter.domain.usecase

import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.repository.JobsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetOffersUseCase(
    private val repository: JobsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    fun getOffers(): Flow<List<Offer>> {
        return repository.getOffers().flowOn(dispatcher)
    }
}