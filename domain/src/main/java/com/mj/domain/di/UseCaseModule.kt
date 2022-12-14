package com.mj.domain.di

import com.mj.domain.repository.SearchRepository
import com.mj.domain.usecase.GetRemoteNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {

//    @Provides
//    @Singleton
//    fun getLocalImageUseCase(repository: ImageRepository): GetLocalImageUseCase = GetLocalImageUseCase(repository)

    @Provides
    @Singleton
    fun getRemoteNewsUseCase(repository: SearchRepository): GetRemoteNewsUseCase = GetRemoteNewsUseCase(repository)
}