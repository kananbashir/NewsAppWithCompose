package com.example.composenewscatcher.ui.navigation

import com.example.composenewscatcher.ui.navigation.util.constant.NavigationRouteConstants

sealed class Screen(val route: String) {
    object Home: Screen(NavigationRouteConstants.HOME_SCREEN)
    object Detail: Screen(NavigationRouteConstants.DETAIL_SCREEN)
    object Saved: Screen(NavigationRouteConstants.SAVED_ARTICLES_SCREEN)
    object LanguageSelection: Screen(NavigationRouteConstants.LANGUAGE_SELECTION_SCREEN)
}