package com.example.composenewscatcher.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.navigation.helper.CustomNavigationBar
import com.example.composenewscatcher.ui.navigation.helper.Screen
import com.example.composenewscatcher.ui.screen.SharedScreenEvent
import com.example.composenewscatcher.ui.screen.SharedScreenState
import com.example.composenewscatcher.ui.screen.SharedViewModel
import com.example.composenewscatcher.ui.screen.home.ArticleDetailsImageSection
import com.example.composenewscatcher.ui.screen.home.HomeScreen
import com.example.composenewscatcher.ui.screen.home.HomeViewModel
import com.example.composenewscatcher.ui.screen.language.LanguageScreenEvent
import com.example.composenewscatcher.ui.screen.language.LanguageSelectionScreen
import com.example.composenewscatcher.ui.screen.saved.SavedScreen
import com.example.composenewscatcher.ui.screen.saved.SavedViewModel
import com.example.composenewscatcher.ui.theme.ArgentinianBlue
import com.example.composenewscatcher.ui.util.GrayIndicatorBox
import com.example.composenewscatcher.ui.util.slideInWithFadeIn
import com.example.composenewscatcher.ui.util.slideOutWithFadeOut
import com.example.composenewscatcher.util.extension.firstLetterCapitalize
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    savedViewModel: SavedViewModel,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier,
    onAppScreenEvent: (SharedScreenEvent) -> Unit,
    onLanguageScreenEvent: (LanguageScreenEvent) -> Unit
) {
    val appScreenState = sharedViewModel.sharedScreenState.collectAsState()
    val homeScreenState = homeViewModel.homeScreenState.collectAsState()
    val savedScreenState = savedViewModel.savedScreenState.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetSwipeEnabled = true,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetScaffoldContent(appScreenState)
        },
        sheetPeekHeight = 0.dp
    ) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                CustomNavigationBar(
                    navController = navController,
                    navBarItems = sharedViewModel.navBarItems,
                    currentRoute = sharedViewModel.currentRoute.collectAsState(),
                    onCurrentRouteChanged = sharedViewModel::updateCurrentRouteDestination
                )
            }
        ) { padding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(route = Screen.Home.route) {
                    HomeScreen(
                        modifier = modifier
                            .fillMaxSize(),
                        scaffoldState = scaffoldState,
                        homeScreenState = homeScreenState,
                        sharedScreenState = appScreenState,
                        coroutineScope = scope,
                        onSharedScreenEvent = sharedViewModel::onSharedScreenEvent,
                        onHomeScreenEvent = homeViewModel::onHomeScreenEvent,
                        onLanguageSelectionButtonClicked = {
                            navController.navigate(Screen.LanguageSelection.route)
                        }
                    )
                }

                composable(route = Screen.Saved.route) {
                    SavedScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 25.dp
                            ),
                        sharedScreenState = appScreenState,
                        savedScreenState = savedScreenState,
                        scaffoldState = scaffoldState,
                        onAppScreenEvent = sharedViewModel::onSharedScreenEvent
                    )
                }

                composable(
                    route = Screen.LanguageSelection.route,
                    enterTransition = {
                        slideIn(
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = LinearOutSlowInEasing
                            )
                        ) { fullSize ->
                            IntOffset(0, fullSize.width*2)
                        }
                    },
                    exitTransition = {
                        slideOut(
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) { fullSize ->
                            IntOffset(0, fullSize.width*2)
                        }
                    }
                ) {
                    LanguageSelectionScreen(
                        modifier = Modifier
                                .fillMaxSize(),
                        onLanguageSelected = {
                            onLanguageScreenEvent(LanguageScreenEvent.UpdateAppLanguage(it, context))
                            onAppScreenEvent(SharedScreenEvent.UpdateSharedLanguageState(it))
                            navController.popBackStack()
                        },
                        onReturnButtonClicked = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible =
            !(appScreenState.value.requestedArticle != null
                    && scaffoldState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded),
            enter = slideInWithFadeIn(),
            exit = slideOutWithFadeOut()
        ) {
            appScreenState.value.requestedArticle?.let {
                ArticleDetailsImageSection(
                    sharedScreenState = appScreenState.value,
                    onReturnButtonClicked = {
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                            onAppScreenEvent(SharedScreenEvent.UpdateRequestedArticle(null))
                        }
                    },
                    onSaveButtonClicked = {
                        onAppScreenEvent(SharedScreenEvent.UpdateArticleSavedStatus(appScreenState.value.requestedArticle!!))
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomSheetScaffoldContent(
    sharedScreenState: State<SharedScreenState>,
) {
    val bottomSheetHeight = (LocalConfiguration.current.screenHeightDp * 75 / 100)
    val uriHandler = LocalUriHandler.current

    sharedScreenState.value.requestedArticle?.let { requestedArticle ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = bottomSheetHeight.dp, min = bottomSheetHeight.dp)
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
                color = ArgentinianBlue
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        requestedArticle.link?.let {
                            uriHandler.openUri(it)
                        }
                    },
                text = stringResource(R.string.text_read_more),
                fontSize = 13.sp,
                textAlign = TextAlign.End,
                color = ArgentinianBlue
            )

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}