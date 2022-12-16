package com.mj.domain.model.encyc

data class EncycData(
    val total: Int,
    val title: String, //백과사전 표제어 <b> 태그 존재, htmlText
    val link: String, //백과사전 항목 설명 URL
    val description: String, //백과사전 항목 설명의 요약 정보 <b> 태그 존재, htmlText
    val thumbnail: String //섬네일
)