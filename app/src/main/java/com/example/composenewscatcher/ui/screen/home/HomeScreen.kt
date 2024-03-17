package com.example.composenewscatcher.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.util.DateAndLanguageSection
import com.example.composenewscatcher.ui.util.GrayIndicatorBox
import com.example.composenewscatcher.ui.util.HeaderSection
import com.example.composenewscatcher.ui.util.slideInWithFadeIn
import com.example.composenewscatcher.ui.util.slideOutWithFadeOut
import com.example.composenewscatcher.ui.theme.SeaBlue
import com.example.composenewscatcher.util.extension.firstLetterCapitalize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenState: State<HomeScreenState>,
    modifier: Modifier = Modifier,
    onHomeScreenEvent: (HomeScreenEvent) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

//    LaunchedEffect(scaffoldState) {
//        snapshotFlow { scaffoldState.bottomSheetState.requireOffset() }.collect { offset ->
//            homeScreenState.value.requestedArticle?.let {
//                if (offset == 1000f) {
//                    onHomeScreenEvent(HomeScreenEvent.UpdateRequestedArticle(null))
//                }
//            }
//        }
//    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        sheetSwipeEnabled = true,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetScaffoldContent(homeScreenState)
        },
        sheetPeekHeight = 0.dp
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            DateAndLanguageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                withLanguageSelector = true
            )

            Spacer(modifier = Modifier.height(30.dp))


            LatestHeadlinesSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                homeScreenState = homeScreenState.value,
                onArticleItemClicked = {
                    scope.launch {
                        onHomeScreenEvent(HomeScreenEvent.UpdateRequestedArticle(it))
                        scaffoldState.bottomSheetState.expand()
                    }
                },
            )

            Spacer(modifier = Modifier.height(30.dp))

            SearchBarSection(
                homeScreenState = homeScreenState.value,
                onSearchFieldValueChanged = {
                    onHomeScreenEvent(HomeScreenEvent.UpdateSearchFieldInput(it))
                },
                onSearchRequested = {
                    onHomeScreenEvent(HomeScreenEvent.SearchForArticles(homeScreenState.value.searchFieldInput))
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SearchResultsSection(
                homeScreenState = homeScreenState.value,
                modifier = Modifier.weight(1f),
                onSearchArticleClicked = {
                    scope.launch {
                        onHomeScreenEvent(HomeScreenEvent.UpdateRequestedArticle(it))
                        scaffoldState.bottomSheetState.expand()
                    }
                })
        }

        AnimatedVisibility(
            visible =
            !(homeScreenState.value.requestedArticle != null
                    && scaffoldState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded),
            enter = slideInWithFadeIn(),
            exit = slideOutWithFadeOut()
        ) {
            homeScreenState.value.requestedArticle?.let {
                ArticleDetailsImageSection(
                    homeScreenState = homeScreenState.value,
                    onReturnButtonClicked = {
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                            onHomeScreenEvent(HomeScreenEvent.UpdateRequestedArticle(null))
                        }
                    },
                    onSaveButtonClicked = {
                        onHomeScreenEvent(HomeScreenEvent.UpdateArticleSavedStatus(homeScreenState.value.requestedArticle!!))
                    }
                )
            }
        }
    }


    BackHandler(
        enabled = homeScreenState.value.requestedArticle != null
    ) {
        scope.launch {
            scaffoldState.bottomSheetState.partialExpand()
            onHomeScreenEvent(HomeScreenEvent.UpdateRequestedArticle(null))
        }
    }
}

@Composable
private fun BottomSheetScaffoldContent(
    homeScreenState: State<HomeScreenState>
) {
    homeScreenState.value.requestedArticle?.let { requestedArticle ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 540.dp, min = 540.dp)
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            GrayIndicatorBox {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    text = requestedArticle.topic?.firstLetterCapitalize() ?: stringResource(R.string.placeholder_text_no_topic),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = requestedArticle.title ?: stringResource(R.string.placeholder_text_no_author),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = requestedArticle.author ?: stringResource(R.string.placeholder_text_no_author),
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                color = SeaBlue
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = requestedArticle.excerpt ?: stringResource(R.string.placeholder_text_no_excerpt),
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = requestedArticle.summary ?: stringResource(R.string.placeholder_text_no_summary),
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.text_read_more),
                fontSize = 13.sp,
                textAlign = TextAlign.End,
                color = SeaBlue
            )

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}