package com.example.composenewscatcher.ui.screen.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.model.NewsResponse
import com.example.composenewscatcher.data.remote.newscatcherapi.helper.NetworkResult
import com.example.composenewscatcher.data.repository.DataStoreRepository
import com.example.composenewscatcher.data.repository.NewsCatcherRepository
import com.example.composenewscatcher.util.constant.CacheConstants.CACHE_POLICY_MIN_MINUTE
import com.example.composenewscatcher.util.extension.excludeSavedArticles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsCatcherRepository: NewsCatcherRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application,
) : AndroidViewModel(application) {

    private var _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private var cachedArticles = newsCatcherRepository.LocalDataSource().newsCatcher.getAllCachedArticles()
    private var languagePreferenceFlow: Flow<String> = dataStoreRepository.languagePreferenceFlow

    init {
        viewModelScope.launch {
            cachedArticles.collect { cachedArticles ->
                val language = languagePreferenceFlow.first()

                if (cachedArticles.isNotEmpty()) {
                    val lastTimeCached = LocalTime.parse(cachedArticles.first().cachedTime)
                    if (ChronoUnit.MINUTES.between(
                            lastTimeCached,
                            LocalTime.now().plusMinutes(1)
                        ) > CACHE_POLICY_MIN_MINUTE
                    ) {
                        deleteAll(*cachedArticles.excludeSavedArticles().toTypedArray())
                        getLatestHeadlineNews(language, 1)
                    } else {
                        updateCachedArticlesState(cachedArticles)
                    }
                } else {
                    getLatestHeadlineNews(language, 1)
                }
            }
        }

        viewModelScope.launch {
            homeScreenState.collect {
                if ((it.newsResponseResult is NetworkResult.Error && it.newsResponseResult.message?.isNotBlank()!!) ||
                    (it.searchNewsResponseResult is NetworkResult.Error && it.searchNewsResponseResult.message?.isNotBlank()!!)
                ) {
                    Toast.makeText(application.applicationContext, homeScreenState.value.newsResponseResult.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }


    fun onHomeScreenEvent(homeScreenEvent: HomeScreenEvent) = when (homeScreenEvent) {
        is HomeScreenEvent.UpdateSearchFieldInput -> updateSearchFieldInput(homeScreenEvent.input)
        is HomeScreenEvent.SearchForArticles -> searchForArticles(homeScreenEvent.searchQuery, 1)
    }

    private fun upsertAll(vararg article: Article) = viewModelScope.launch {
        newsCatcherRepository.LocalDataSource().newsCatcher.upsertAll(*article)
    }

    private fun delete(article: Article) = viewModelScope.launch {
        newsCatcherRepository.LocalDataSource().newsCatcher.delete(article)
    }

    private fun deleteAll(vararg article: Article) = viewModelScope.launch {
        newsCatcherRepository.LocalDataSource().newsCatcher.deleteAll(*article)
    }

    private fun getLatestHeadlineNews(language: String, page: Int) {
        viewModelScope.launch {
            apiSafeCallForLatestHeadlines(language, page)
        }
    }

    private fun searchForArticles(searchQuery: String, page: Int) {
        viewModelScope.launch {
            apiSafeCallForSearch(searchQuery, page)
        }
    }

    private suspend fun apiSafeCallForLatestHeadlines(language: String, page: Int) {
        _homeScreenState.value = _homeScreenState.value.copy(
            newsResponseResult = NetworkResult.Loading()
        )

        if (deviceHasInternetConnection()) {
            val response = newsCatcherRepository.RemoteDataSource()
                .newsCatcher
                .fetchLatestHeadlines(
                    language = language,
                    page = page
                )

            handleLatestHeadlinesNewsResponse(response)
        } else {
            _homeScreenState.value = _homeScreenState.value.copy(
                newsResponseResult = NetworkResult.Error(
                    message = "No internet connection"
                )
            )
        }
    }

    private suspend fun apiSafeCallForSearch(searchQuery: String, page: Int) {
        _homeScreenState.value = _homeScreenState.value.copy(
            searchNewsResponseResult = NetworkResult.Loading()
        )

        if (deviceHasInternetConnection()) {
            val response = newsCatcherRepository.RemoteDataSource()
                .newsCatcher
                .searchInNews(
                    language = languagePreferenceFlow.first(),
                    queryKey = searchQuery,
                    page = page
                )

            handleSearchNewsResponse(response)
        } else {
            _homeScreenState.value = _homeScreenState.value.copy(
                searchNewsResponseResult = NetworkResult.Error(
                    message = "No internet connection"
                )
            )
        }
    }

    private fun handleLatestHeadlinesNewsResponse(newsResponse: Response<NewsResponse>) {
        when (newsResponse.body()?.status) {
            "error" -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    newsResponseResult = NetworkResult.Error(message = newsResponse.message())
                )
            }

            "ok" -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    newsResponseResult = NetworkResult.Success(data = newsResponse.body())
                )
                cacheLatestArticles()
            }

            else -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    newsResponseResult = NetworkResult.Error(message = newsResponse.message())
                )
            }
        }
    }

    private fun handleSearchNewsResponse(newsResponse: Response<NewsResponse>) {
        when (newsResponse.body()?.status) {
            "error" -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    searchNewsResponseResult = NetworkResult.Error(message = newsResponse.message())
                )
            }

            "ok" -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    searchNewsResponseResult = NetworkResult.Success(data = newsResponse.body())
                )
            }

            else -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    searchNewsResponseResult = NetworkResult.Error(message = newsResponse.message())
                )
            }
        }
    }

    private fun cacheLatestArticles() {
        val allArticles = homeScreenState.value.newsResponseResult.data?.articles?.toMutableList()
        val cachedTime = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)

        allArticles?.let { articleList ->
            val updatedArticles = articleList.mapNotNull { article ->
                article?.let {
                    it.isCached = true
                    it.cachedTime = cachedTime
                    it
                }
            }

            upsertAll(*updatedArticles.toTypedArray())
        }

    }

    private fun updateSearchFieldInput(input: String) {
        _homeScreenState.value = _homeScreenState.value.copy(
            searchFieldInput = input
        )
    }

    private fun updateCachedArticlesState(cachedArticles: List<Article>) {
        _homeScreenState.value = _homeScreenState.value.copy(
            cachedArticles = cachedArticles,
            newsResponseResult = NetworkResult.Error(message = "")
        )
    }

    private fun deviceHasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
        val capabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}