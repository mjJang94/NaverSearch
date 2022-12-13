package com.mj.data.di

import com.mj.data.remote.NaverSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private val BASE_URL = "https://openapi.naver.com"
    private val NAVER_CLIENT_ID = "K1qiZunVsQP0Tv4lRBE0"
    private val NAVER_CLIENT_SECRET = "V4Q5mGl270"

    @Provides
    @Singleton
    fun provideApi(): NaverSearchService {
        val logger = HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BASIC
        }
        val interceptor = Interceptor { chain ->
            with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                    .build()
                proceed(newRequest)
            }
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverSearchService::class.java)
    }
}