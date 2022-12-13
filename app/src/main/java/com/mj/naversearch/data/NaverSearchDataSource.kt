package com.mj.naversearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.domain.model.news.NewsData
import com.mj.domain.usecase.GetRemoteSearchUseCase

class NaverSearchDataSource(
    private val query: String,
    private val searchUseCase: GetRemoteSearchUseCase
) : PagingSource<Int, NewsData>() {
    override fun getRefreshKey(state: PagingState<Int, NewsData>): Int? {
        return state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsData> {
        val start = params.key ?: defaultStart

        return try {
            val response = searchUseCase.searchNews(query, 30, start)
            val nextKey = if (response.isEmpty()) null else start + 1
            val prevKey = if (start == defaultStart) null else start - defaultDisplay
            LoadResult.Page(response, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val defaultStart = 1
        const val defaultDisplay = 10
    }
}