package com.mj.data.repository.remote

import com.mj.data.model.news.NewsResponse
import kotlinx.coroutines.flow.Flow

interface SearchRemoteDataSource {
    suspend fun getRemoteSearch(query: String, loadSize: Int, start: Int): NewsResponse
}