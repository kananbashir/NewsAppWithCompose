package com.example.composenewscatcher.ui.screen.saved

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composenewscatcher.ui.screen.SharedScreenEvent
import com.example.composenewscatcher.ui.screen.SharedScreenState
import com.example.composenewscatcher.ui.util.DateAndLanguageSection
import com.example.composenewscatcher.ui.util.HeaderSection
import com.example.composenewscatcher.ui.util.LazyColumnArticleItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    sharedScreenState: State<SharedScreenState>,
    savedScreenState: State<SavedScreenState>,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    onAppScreenEvent: (SharedScreenEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

    BackHandler(
        enabled = sharedScreenState.value.requestedArticle != null
    ) {
        scope.launch {
            scaffoldState.bottomSheetState.partialExpand()
            onAppScreenEvent(SharedScreenEvent.UpdateRequestedArticle(null))
        }
    }

    Column(modifier = modifier) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(35.dp))
        DateAndLanguageSection(
            modifier = Modifier.padding(0.dp),
            withLanguageSelector = false,
            sharedScreenState = sharedScreenState.value
        )
        Spacer(modifier = Modifier.height(35.dp))
        LazyColumn {
            items(savedScreenState.value.savedArticles) { article ->
                LazyColumnArticleItem(
                    article,
                    onArticleItemClicked = {
                        scope.launch {
                            onAppScreenEvent(SharedScreenEvent.UpdateRequestedArticle(article))
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                )
            }
        }
    }
}