package com.example.composenewscatcher.ui.screen.home

import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.model.NewsResponse
import com.example.composenewscatcher.data.remote.newscatcherapi.helper.NetworkResult

data class HomeScreenState(
    val newsResponseResult: NetworkResult<NewsResponse> = NetworkResult.Loading(),
    val cachedArticles: List<Article> = listOf(),
    val searchNewsResponseResult: NetworkResult<NewsResponse>? = null,
    val searchFieldInput: String = "",
    val preferredLanguage: String? = null
)
