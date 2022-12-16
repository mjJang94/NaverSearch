package com.mj.data.remote

import com.mj.data.model.book.BookResponse
import com.mj.data.model.encyc.EncyclopediaResponse
import com.mj.data.model.news.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverSearchService {

    @GET("/v1/search/news.json")
    suspend fun getNews(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
    ): NewsResponse

    @GET("/v1/search/book.json")
    suspend fun getBooks(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
    ): BookResponse

    @GET("/v1/search/encyc.json")
    suspend fun getEncyclopedia(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
    ): EncyclopediaResponse
}