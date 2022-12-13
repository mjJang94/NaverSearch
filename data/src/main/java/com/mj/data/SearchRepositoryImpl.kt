package com.mj.data

import com.mj.data.model.news.NewsItem
import com.mj.data.repository.remote.SearchRemoteDataSource
import com.mj.domain.model.news.NewsData
import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDateSource: SearchRemoteDataSource,
) : SearchRepository {

    override suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): List<NewsData> =
        searchRemoteDateSource.getRemoteSearch(query, loadSize, start).items.favoriteEntityToThumbnailList()


    private fun List<NewsItem>.favoriteEntityToThumbnailList(): List<NewsData> =
        this.map {
            NewsData(it.title, it.originallink, it.link, it.description, it.pubDate)
        }
}