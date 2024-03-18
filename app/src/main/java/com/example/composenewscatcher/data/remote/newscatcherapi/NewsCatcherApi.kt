package com.example.composenewscatcher.data.remote.newscatcherapi

import com.example.composenewscatcher.data.model.NewsResponse
import com.example.composenewscatcher.util.constant.NewsCatcherApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCatcherApi {

    @GET("latest_headlines")
    suspend fun fetchLatestHeadlines(
        @Query(NewsCatcherApiConstants.QueryParameter.LANGUAGE)
        language: String,
        @Query(NewsCatcherApiConstants.QueryParameter.PAGE_SIZE)
        pageSize: Int = NewsCatcherApiConstants.PAGE_SIZE,
        @Query(NewsCatcherApiConstants.QueryParameter.PAGE)
        page: Int
    ): Response<NewsResponse>

    @GET("search")
    suspend fun searchInNews(
        @Query(NewsCatcherApiConstants.QueryParameter.QUERY_KEY)
        queryKey: String,
        @Query(NewsCatcherApiConstants.QueryParameter.LANGUAGE)
        language: String,
        @Query(NewsCatcherApiConstants.QueryParameter.PAGE_SIZE)
        pageSize: Int = NewsCatcherApiConstants.PAGE_SIZE,
        @Query(NewsCatcherApiConstants.QueryParameter.PAGE)
        page: Int
    ): Response<NewsResponse>
}