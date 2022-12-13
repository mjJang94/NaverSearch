package com.mj.data.di

import com.mj.data.remote.NaverSearchService
import com.mj.data.repository.remote.SearchRemoteDataSource
import com.mj.data.repository.remote.SearchRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Provides
    @Singleton
    fun imageData(service: NaverSearchService): SearchRemoteDataSource =
        SearchRemoteDataSourceImpl(service)
}