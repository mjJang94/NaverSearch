package com.mj.domain.model.books

data class BookData(
    val total: Int,
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val discount: Int?, //판매 가격, 품절이거나 하면 값이 없음
    val publisher: String,
    val description: String,
    val publishDate: String
)
