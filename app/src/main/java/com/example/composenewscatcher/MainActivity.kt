package com.example.composenewscatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.composenewscatcher.ui.navigation.SetupNavGraph
import com.example.composenewscatcher.ui.screen.SharedViewModel
import com.example.composenewscatcher.ui.screen.home.HomeViewModel
import com.example.composenewscatcher.ui.theme.ComposeNewsCatcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsCatcherTheme {
                val navController = rememberNavController()
                val homeViewModel: HomeViewModel = viewModel()
                val sharedViewModel: SharedViewModel = viewModel()
//                val language = sharedViewModel.preferredLanguage.collectAsState(initial = "xx")
//                val coroutineScope = rememberCoroutineScope()

//                coroutineScope.launch {
//                    homeViewModel.shouldRequestNewArticles.collect {
//                        if (it) {
//                            homeViewModel.onHomeScreenEvent(
//                                HomeScreenEvent.FetchLatestArticles(language.value)
//                            )
//                        } else {
//                            homeViewModel.onHomeScreenEvent(
//                                HomeScreenEvent.GetCachedArticles
//                            )
//                        }
//                    }
//                }


                SetupNavGraph(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController,
                    homeViewModel = homeViewModel,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}