package com.example.composenewscatcher.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composenewscatcher.ui.screen.SharedScreenEvent
import com.example.composenewscatcher.ui.screen.SharedScreenState
import com.example.composenewscatcher.ui.util.DateAndLanguageSection
import com.example.composenewscatcher.ui.util.HeaderSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenState: State<HomeScreenState>,
    sharedScreenState: State<SharedScreenState>,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    onSharedScreenEvent: (SharedScreenEvent) -> Unit,
    onHomeScreenEvent: (HomeScreenEvent) -> Unit,
    onLanguageSelectionButtonClicked: () -> Unit
) {

    BackHandler(
        enabled = sharedScreenState.value.requestedArticle != null
    ) {
        coroutineScope.launch {
            scaffoldState.bottomSheetState.partialExpand()
            onSharedScreenEvent(SharedScreenEvent.UpdateRequestedArticle(null))
        }
    }

    Column(
        modifier = modifier,
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
            withLanguageSelector = true,
            sharedScreenState = sharedScreenState.value
        ) { onLanguageSelectionButtonClicked() }

        Spacer(modifier = Modifier.height(30.dp))


        LatestHeadlinesSection(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            homeScreenState = homeScreenState.value,
            onArticleItemClicked = {
                coroutineScope.launch {
                    onSharedScreenEvent(SharedScreenEvent.UpdateRequestedArticle(it))
                    scaffoldState.bottomSheetState.expand()
                }
            },
        )

        Spacer(modifier = Modifier.height(30.dp))

        SearchBarSection(
            homeScreenState = homeScreenState.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .border(1.5.dp, Color.LightGray, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp)),
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
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .weight(1f),
            onSearchArticleClicked = {
                coroutineScope.launch {
                    onSharedScreenEvent(SharedScreenEvent.UpdateRequestedArticle(it))
                    scaffoldState.bottomSheetState.expand()
                }
            })
    }
}