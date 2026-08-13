package com.shetty.mapbooking.di

import com.shetty.mapbooking.data.remote.AirQualityApi
import com.shetty.mapbooking.data.remote.ReverseGeocodingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // ----------------------------------------
    // AQI Retrofit
    // ----------------------------------------

    @Provides
    @Singleton
    @AqiRetrofit
    fun provideAqiRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.waqi.info/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ----------------------------------------
    // Geocoding Retrofit
    // ----------------------------------------

    @Provides
    @Singleton
    @GeocodingRetrofit
    fun provideGeocodingRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.bigdatacloud.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ----------------------------------------
    // AQI API
    // ----------------------------------------

    @Provides
    @Singleton
    fun provideAirQualityApi(
        @AqiRetrofit retrofit: Retrofit
    ): AirQualityApi {
        return retrofit.create(AirQualityApi::class.java)
    }

    // ----------------------------------------
    // Geocoding API
    // ----------------------------------------

    @Provides
    @Singleton
    fun provideReverseGeocodingApi(
        @GeocodingRetrofit retrofit: Retrofit
    ): ReverseGeocodingApi {
        return retrofit.create(
            ReverseGeocodingApi::class.java
        )
    }
}