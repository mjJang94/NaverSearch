package com.mj.domain.repository

import com.mj.domain.model.books.BookData
import com.mj.domain.model.news.NewsData

interface SearchRepository {
    suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): List<NewsData>
    suspend fun getRemoteBooksData(query: String, loadSize: Int, start: Int): List<BookData>
}