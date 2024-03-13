package com.example.composenewscatcher.ui.navigation.helper

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationBarItem (
    val itemTitle: String,
    val navRoute: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNotification: Boolean
)