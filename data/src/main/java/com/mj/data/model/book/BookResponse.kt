package com.mj.data.model.book

data class BookResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<BookItem>
)
