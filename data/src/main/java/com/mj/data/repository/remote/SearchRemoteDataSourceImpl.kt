package com.mj.data.repository.remote

import com.mj.data.model.news.NewsResponse
import com.mj.data.remote.NaverSearchService
import javax.inject.Inject

internal class SearchRemoteDataSourceImpl @Inject constructor(
    private val service: NaverSearchService,
) : SearchRemoteDataSource {

//    override suspend fun getRemoteSearch(query: String, loadSize: Int, start: Int): SearchResponse =
//        service.getImages(query, loadSize, start)

    override suspend fun getRemoteSearch(query: String, loadSize: Int, start: Int): NewsResponse =
            service.getNews(query, loadSize, start)
}