package com.jobhunter.app.di

import android.content.Context
import com.jobhunter.data.repository.JobsRepositoryImpl
import com.jobhunter.data.retrofit.JobsApiService
import com.jobhunter.data.room.AppDatabase
import com.jobhunter.domain.repository.JobsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule(private val context: Context) {

    @Provides
    fun provideJobsRepository(
        retrofitService: JobsApiService,
        database: AppDatabase
    ): JobsRepository {
        return JobsRepositoryImpl(
            retrofitService = retrofitService,
            likedVacanciesDao = database.vacanciesDao()
        )
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

    @Provides
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
}