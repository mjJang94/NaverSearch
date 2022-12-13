package com.mj.domain.repository

import com.mj.domain.model.news.NewsData

interface SearchRepository {
    suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): List<NewsData>
//    suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData>
//    suspend fun saveImages(data: ThumbnailData)
//    suspend fun deleteImages(uid: Long)
}