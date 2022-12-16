package com.mj.domain.model.news

data class NewsDataResponse(
    val total: Int,
    val items: List<NewsData>
)
