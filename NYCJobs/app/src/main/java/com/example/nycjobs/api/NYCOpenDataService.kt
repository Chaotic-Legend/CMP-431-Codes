package com.example.nycjobs.api

import android.util.Log
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.util.TAG
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the NYC Open Data API.
 *
 * This interface defines the API endpoints for the NYC Open Data API.
 */
interface NYCOpenDataService {
    @GET("kpav-sd4t.json?posting_type=External&\$order=posting_updated%20DESC&")
    suspend fun getJobPostings(
        @Query("\$limit") limit: Int = 50,
        @Query("\$offset") offset: Int
    ): List<JobPost>
}

/**
 * App Remote APIs
 *
 * This class defines the remote APIs for the app.
 */
class AppRemoteApis {
    private val baseUrl = "https://data.cityofnewyork.us/resource/"
    private val contentType = "application/json; charset=utf-8".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Get the API calls.
     *
     * @return the interface that defines the API endpoints.
     */
    fun getNycOpenDataApi(): NYCOpenDataService {
        Log.i(TAG, "Retrofit Service Creating API Calls")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NYCOpenDataService::class.java)
    }
}