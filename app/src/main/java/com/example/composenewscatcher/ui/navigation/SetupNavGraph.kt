package com.example.composenewscatcher.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composenewscatcher.ui.navigation.helper.CustomNavigationBar
import com.example.composenewscatcher.ui.navigation.helper.Screen
import com.example.composenewscatcher.ui.screen.SharedViewModel
import com.example.composenewscatcher.ui.screen.home.HomeScreen
import com.example.composenewscatcher.ui.screen.home.HomeViewModel
import com.example.composenewscatcher.ui.screen.saved.SavedScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier,
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
                    homeViewModel.homeScreenState.collectAsState(),
                    onHomeScreenEvent = homeViewModel::onHomeScreenEvent
                )
            }

            composable(route = Screen.Saved.route) {
                SavedScreen(
                    homeViewModel.homeScreenState.collectAsState(),
                    onHomeScreenEvent = homeViewModel::onHomeScreenEvent
                )
            }
        }
    }
}