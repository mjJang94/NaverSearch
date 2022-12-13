package com.mj.data.di

import com.mj.data.SearchRepositoryImpl
import com.mj.data.repository.remote.SearchRemoteDataSource
import com.mj.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideImageRepository(
        searchRemoteDataSource: SearchRemoteDataSource,
    ): SearchRepository {
        return SearchRepositoryImpl(searchRemoteDataSource)
    }
}