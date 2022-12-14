package com.mj.domain.usecase

import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class GetRemoteNewsUseCase @Inject constructor(private val repository: SearchRepository){
    suspend fun searchNews(query: String, size: Int, start: Int) = repository.getRemoteNewsData(query, size, start)
}