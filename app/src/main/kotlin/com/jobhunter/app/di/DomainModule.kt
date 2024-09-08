package com.jobhunter.app.di

import com.jobhunter.domain.repository.JobsRepository
import com.jobhunter.domain.usecase.GetAllVacanciesUseCase
import com.jobhunter.domain.usecase.GetLikedVacanciesUseCase
import com.jobhunter.domain.usecase.GetOffersUseCase
import com.jobhunter.domain.usecase.GetVacancyUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class DomainModule {
    @Provides
    fun provideGetAllVacanciesUseCase(repository: JobsRepository) = GetAllVacanciesUseCase(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    @Provides
    fun provideGetOffersUseCase(repository: JobsRepository) = GetOffersUseCase(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    @Provides
    fun provideGetLikedVacanciesUseCase(repository: JobsRepository) = GetLikedVacanciesUseCase(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    @Provides
    fun provideLikeVacancyUseCase(repository: JobsRepository) = LikeVacancyUseCase(
        repository = repository,
        dispatcher = Dispatchers.IO
    )

    @Provides
    fun provideGetVacancyUseCase(repository: JobsRepository) = GetVacancyUseCase(
        repository = repository,
        dispatcher = Dispatchers.IO
    )
}