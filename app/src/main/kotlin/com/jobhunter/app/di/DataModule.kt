package com.jobhunter.app.di

import com.jobhunter.data.repository.JobsRepositoryImpl
import com.jobhunter.data.retrofit.JobsApiService
import com.jobhunter.domain.repository.JobsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule {

    @Provides
    fun provideJobsRepository(retrofitService: JobsApiService): JobsRepository {
        return JobsRepositoryImpl(retrofitService)
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): JobsApiService {
        return retrofit.create(JobsApiService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}