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
import com.example.composenewscatcher.ui.screen.language.LanguageViewModel
import com.example.composenewscatcher.ui.screen.saved.SavedViewModel
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
                val savedViewModel: SavedViewModel = viewModel()
                val languageViewModel: LanguageViewModel = viewModel()
                val sharedViewModel: SharedViewModel = viewModel()

                SetupNavGraph(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController,
                    homeViewModel = homeViewModel,
                    savedViewModel = savedViewModel,
                    sharedViewModel = sharedViewModel,
                    onAppScreenEvent = sharedViewModel::onSharedScreenEvent,
                    onLanguageScreenEvent = languageViewModel::onLanguageScreenEvent
                )
            }
        }
    }
}