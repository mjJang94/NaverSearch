package com.mj.domain.usecase

import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class GetRemoteBookUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend fun searchBooks(query: String, size: Int, start: Int) = repository.getRemoteBooksData(query, size, start)
}