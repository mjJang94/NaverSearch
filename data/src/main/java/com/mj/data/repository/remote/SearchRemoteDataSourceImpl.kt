package com.mj.data.repository.remote

import com.mj.data.model.book.BookResponse
import com.mj.data.model.news.NewsResponse
import com.mj.data.remote.NaverSearchService
import javax.inject.Inject

internal class SearchRemoteDataSourceImpl @Inject constructor(
    private val service: NaverSearchService,
) : SearchRemoteDataSource {

    override suspend fun getRemoteNews(query: String, loadSize: Int, start: Int): NewsResponse =
        service.getNews(query, loadSize, start)

    override suspend fun getRemoteBooks(query: String, loadSize: Int, start: Int): BookResponse =
        service.getBooks(query, loadSize, start)
}