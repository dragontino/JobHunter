package com.jobhunter.data.retrofit

import com.jobhunter.data.model.JobApi
import retrofit2.http.GET

interface JobsApiService {
    @GET("u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download/")
    suspend fun getJobApi(): JobApi
}