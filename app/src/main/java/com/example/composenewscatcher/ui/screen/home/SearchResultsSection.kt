package com.example.composenewscatcher.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.remote.newscatcherapi.helper.NetworkResult
import com.example.composenewscatcher.ui.util.LazyColumnArticleItem

@Composable
fun SearchResultsSection(
    homeScreenState: HomeScreenState,
    modifier: Modifier = Modifier,
    onSearchArticleClicked: (article: Article) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = homeScreenState.searchNewsResponseResult is NetworkResult.Success
        ) {
            LazyColumn {
                val articles = homeScreenState.searchNewsResponseResult?.data?.articles
                articles?.let { articleList ->
                    items(articleList) { article ->
                        article?.let {
                            LazyColumnArticleItem(
                                it,
                                onArticleItemClicked = { onSearchArticleClicked(it) })
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = (homeScreenState.searchNewsResponseResult is NetworkResult.Loading)
        ) {
            CircularProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
