package com.mj.data.model.book

import com.google.gson.annotations.SerializedName

data class BookItem(
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val discount: Int?, //판매 가격, 품절이거나 하면 값이 없음
    val publisher: String,
    val description: String,
    @SerializedName("pubdate") val publishDate: String
)
