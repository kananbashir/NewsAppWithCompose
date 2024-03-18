package com.example.composenewscatcher.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composenewscatcher.R
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.data.remote.newscatcherapi.helper.NetworkResult
import com.example.composenewscatcher.ui.util.GrayIndicatorBox
import com.example.composenewscatcher.util.extension.firstLetterCapitalize

@Composable
fun LatestHeadlinesSection(
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    onArticleItemClicked: (article: Article) -> Unit,
) {
    val articles = when {
        homeScreenState.cachedArticles.isNotEmpty() -> {
            homeScreenState.cachedArticles
        }

        homeScreenState.newsResponseResult is NetworkResult.Success -> {
            homeScreenState.newsResponseResult.data?.articles
        }

        else -> {
            null
        }
    }

    AnimatedVisibility(
        visible = articles != null
    ) {
        articles?.let { articleList ->
            LazyRow(modifier = modifier) {
                items(articleList.size) { index ->
                    val article = articleList[index]
                    article?.let { currentArticle ->
                        LatestHeadlineArticleItem(
                            currentArticle,
                            modifier = Modifier
                                .size(350.dp, 220.dp)
                                .clip(RoundedCornerShape(30.dp)),
                            endPadding = if (index == (articleList.size - 1)) 20.dp else 0.dp,
                            onArticleItemClicked = {
                                onArticleItemClicked(currentArticle)
                            }
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = homeScreenState.newsResponseResult is NetworkResult.Loading
    ) {
        CircularProgressIndicator(
            trackColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun LatestHeadlineArticleItem(
    article: Article,
    endPadding: Dp,
    modifier: Modifier = Modifier,
    onArticleItemClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(start = 20.dp, end = endPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onArticleItemClicked() }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.media ?: painterResource(R.drawable.placeholder_no_image_available))
                .crossfade(true)
                .error(R.drawable.placeholder_no_image_available)
                .build(),
            contentDescription = "Headline article",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            GrayIndicatorBox(
                boxAlpha = 0.7f,
                modifier = Modifier.padding(28.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 30.dp,
                            end = 30.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    text = article.topic?.firstLetterCapitalize() ?: stringResource(R.string.placeholder_text_no_topic),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f))
                    .padding(18.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = article.title ?: stringResource(R.string.placeholder_text_no_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}