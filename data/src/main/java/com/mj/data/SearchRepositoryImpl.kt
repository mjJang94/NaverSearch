package com.mj.data

import com.mj.data.model.book.BookItem
import com.mj.data.model.news.NewsItem
import com.mj.data.repository.remote.SearchRemoteDataSource
import com.mj.domain.model.books.BookData
import com.mj.domain.model.news.NewsData
import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDateSource: SearchRemoteDataSource,
) : SearchRepository {

    override suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): List<NewsData> =
        searchRemoteDateSource.getRemoteNews(query, loadSize, start).items.newsItemToData()


    override suspend fun getRemoteBooksData(query: String, loadSize: Int, start: Int): List<BookData> =
        searchRemoteDateSource.getRemoteBooks(query, loadSize, start).items.bookItemToData()


    private fun List<NewsItem>.newsItemToData(): List<NewsData> =
        this.map {
            NewsData(it.title, it.originallink, it.link, it.description, it.pubDate)
        }

    private fun List<BookItem>.bookItemToData(): List<BookData> =
        this.map {
            BookData(
                title = it.title,
                link = it.link,
                image = it.image,
                author = it.author,
                discount = it.discount,
                publisher = it.publisher,
                description = it.description,
                publishDate = it.publishDate
            )
        }
}