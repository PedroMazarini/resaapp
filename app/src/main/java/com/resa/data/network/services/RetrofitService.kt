package com.resa.data.network.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitService {

    fun <T> getInstance(
        clazz: Class<T>,
        baseUrl: String = BASE_URL_AUTH,
    ): T {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi()))
            .client(httpClient.build())
            .build().create(clazz)
    }

    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    const val BASE_URL_AUTH = "https://ext-api.vasttrafik.se/"
    const val BASE_URL_GEO = "https://ext-api.vasttrafik.se/geo/v2/"
    const val BASE_URL_TRAVEL_PLANNER = "https://ext-api.vasttrafik.se/pr/v4/"
}