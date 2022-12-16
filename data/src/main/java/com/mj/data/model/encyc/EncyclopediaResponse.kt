package com.mj.data.model.encyc


data class EncyclopediaResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<EncyclopediaItem>
)
