package com.shetty.mapbooking.di

import com.shetty.mapbooking.data.mock.MockBookingDataSource
import com.shetty.mapbooking.data.remote.BookingDataSource
import com.shetty.mapbooking.data.repository.BookingRepositoryImpl
import com.shetty.mapbooking.data.repository.LocationRepositoryImpl
import com.shetty.mapbooking.domain.repository.BookingRepository
import com.shetty.mapbooking.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookingDataSource(
        implementation: MockBookingDataSource
    ): BookingDataSource

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        implementation: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        implementation: LocationRepositoryImpl
    ): LocationRepository
}