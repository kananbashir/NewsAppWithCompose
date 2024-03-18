package com.example.composenewscatcher.data.remote.newscatcherapi

import com.example.composenewscatcher.data.model.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsCatcherApiRemoteDataSource @Inject constructor(private val newsCatcherApi: NewsCatcherApi) {

    suspend fun fetchLatestHeadlines(language: String, page: Int): Response<NewsResponse> {
        return newsCatcherApi.fetchLatestHeadlines(
            language = language,
            page = page)
    }

    suspend fun searchInNews(queryKey: String, language: String, page: Int): Response<NewsResponse> {
        return newsCatcherApi.searchInNews(
            queryKey = "\"$queryKey\"",
            language = language,
            page = page
        )
    }

}