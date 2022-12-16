package com.mj.data

import com.mj.data.model.book.BookItem
import com.mj.data.model.encyc.EncyclopediaItem
import com.mj.data.model.news.NewsItem
import com.mj.data.repository.remote.SearchRemoteDataSource
import com.mj.domain.model.books.BookData
import com.mj.domain.model.books.BookDataResponse
import com.mj.domain.model.encyc.EncycData
import com.mj.domain.model.encyc.EncycDataResponse
import com.mj.domain.model.news.NewsData
import com.mj.domain.model.news.NewsDataResponse
import com.mj.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDateSource: SearchRemoteDataSource,
) : SearchRepository {

    override suspend fun getRemoteNewsData(query: String, loadSize: Int, start: Int): NewsDataResponse =
        with(searchRemoteDateSource.getRemoteNews(query, loadSize, start)) {
            items.newsItemToData(total)
        }

    override suspend fun getRemoteBooksData(query: String, loadSize: Int, start: Int): BookDataResponse =
        with(searchRemoteDateSource.getRemoteBooks(query, loadSize, start)) {
            items.bookItemToData(total)
        }

    override suspend fun getEncyclopediaData(query: String, loadSize: Int, start: Int): EncycDataResponse =
        with(searchRemoteDateSource.getEncyclopedia(query, loadSize, start)) {
            items.encycItemToData(total)
        }

    private fun List<NewsItem>.newsItemToData(total: Int): NewsDataResponse =
        NewsDataResponse(
            total = total,
            items = this.map {
                NewsData(
                    total = total,
                    title = it.title,
                    originallink = it.originallink,
                    link = it.link,
                    description = it.description,
                    pubDate = it.pubDate
                )
            }
        )

    private fun List<BookItem>.bookItemToData(total: Int): BookDataResponse =
        BookDataResponse(
            total = total,
            items = this.map {
                BookData(
                    total = total,
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
        )


    private fun List<EncyclopediaItem>.encycItemToData(total: Int): EncycDataResponse =
        EncycDataResponse(
            total = total,
            items = this.map {
                EncycData(
                    total = total,
                    title = it.title,
                    link = it.link,
                    description = it.description,
                    thumbnail = it.thumbnail
                )
            }
        )
}