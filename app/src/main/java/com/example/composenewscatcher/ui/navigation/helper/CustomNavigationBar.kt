package com.example.composenewscatcher.ui.navigation.helper

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun CustomNavigationBar(
    navController: NavHostController,
    navBarItems: List<NavigationBarItem>,
    currentRoute: State<String>,
    modifier: Modifier = Modifier,
    onCurrentRouteChanged: (updatedRoute: String) -> Unit,
) {

    rememberCoroutineScope().launch {
        navController.currentBackStack.collect {
            if (currentRoute.value != it.last().destination.route!!) {
                onCurrentRouteChanged(it.last().destination.route!!)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onPrimary)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navBarItems.forEach { navigationBarItem ->
                NavBarItem(
                    navigationBarItem = navigationBarItem,
                    currentRoute = currentRoute.value,
                    onNavigationItemSelected = {
                        if (currentRoute.value != navigationBarItem.navRoute) {
                            if (navController.currentBackStack.value.size == 2) {
                                navController.navigate(navigationBarItem.navRoute)
                            } else {
                                navController.popBackStack(navigationBarItem.navRoute, inclusive = false)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    navigationBarItem: NavigationBarItem,
    currentRoute: String,
    modifier: Modifier = Modifier,
    onNavigationItemSelected: () -> Unit,
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(top = 3.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onNavigationItemSelected()
            },
        contentAlignment = Alignment.TopEnd
    ) {
        NavBarIcon(
            navigationBarItem = navigationBarItem,
            currentRoute = currentRoute
        )

        if (navigationBarItem.hasNotification) {
            RedNotificationDot()
        }
    }
}

@Composable
private fun NavBarIcon(
    navigationBarItem: NavigationBarItem,
    currentRoute: String,
) {
    Icon(
        modifier = Modifier
            .animateContentSize()
            .size(50.dp),
        imageVector = if (navigationBarItem.navRoute == currentRoute) {
            navigationBarItem.selectedIcon
        } else {
            navigationBarItem.unselectedIcon
        },
        contentDescription = "Navigation bar item",
        tint = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun RedNotificationDot(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(Color.Red)
    )
}