package com.example.composenewscatcher.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.navigation.util.constant.NavigationRouteConstants
import com.example.composenewscatcher.ui.navigation.helper.NavigationBarItem
import com.example.composenewscatcher.ui.navigation.util.NoRippleNavigationBar
import com.example.composenewscatcher.ui.screen.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier,
) {
    var selectedNavBarItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                navBarItems.forEachIndexed { index, navigationBarItem ->
                    NavigationBarItem(
                        selected = selectedNavBarItemIndex == index,
                        onClick = {
                            selectedNavBarItemIndex = index
                            navController.navigate(navigationBarItem.navRoute)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (navigationBarItem.hasNotification) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedNavBarItemIndex == index) {
                                        navigationBarItem.selectedIcon
                                    } else {
                                        navigationBarItem.unselectedIcon
                                    },
                                    contentDescription = stringResource(R.string.cont_desc_selected_nav_bar_home),
                                    tint = Color.Black
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .padding(padding),
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen()
            }
        }
    }
}


private val navBarItems = listOf(
    NavigationBarItem(
        itemTitle = "Home",
        navRoute = NavigationRouteConstants.HOME_SCREEN,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNotification = false
    ),
    NavigationBarItem(
        itemTitle = "Saved",
        navRoute = NavigationRouteConstants.DETAIL_SCREEN,
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        hasNotification = false
    )
)