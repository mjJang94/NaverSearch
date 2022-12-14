package com.mj.data.repository.remote

import com.mj.data.model.book.BookResponse
import com.mj.data.model.news.NewsResponse

interface SearchRemoteDataSource {
    suspend fun getRemoteNews(query: String, loadSize: Int, start: Int): NewsResponse
    suspend fun getRemoteBooks(query: String, loadSize: Int, start: Int): BookResponse
}