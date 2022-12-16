package com.mj.domain.usecase

import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class GetRemoteEncycUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend fun searchEncyc(query: String, size: Int, start: Int) = repository.getEncyclopediaData(query, size, start)
}