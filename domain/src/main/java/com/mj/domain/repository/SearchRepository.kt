package com.mj.domain.repository

import com.mj.domain.model.books.BookData
import com.mj.domain.model.books.BookDataResponse
import com.mj.domain.model.encyc.EncycData
import com.mj.domain.model.encyc.EncycDataResponse
import com.mj.domain.model.news.NewsData
import com.mj.domain.model.news.NewsDataResponse

interface SearchRepository {
    suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): NewsDataResponse
    suspend fun getRemoteBooksData(query: String, loadSize: Int, start: Int): BookDataResponse
    suspend fun getEncyclopediaData(query: String, loadSize: Int, start: Int): EncycDataResponse
}