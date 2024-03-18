package com.example.composenewscatcher.ui.navigation.helper

import com.example.composenewscatcher.util.constant.NavigationRouteConstants


sealed class Screen(val route: String) {
    object Home: Screen(NavigationRouteConstants.HOME_SCREEN)
    object Saved: Screen(NavigationRouteConstants.SAVED_ARTICLES_SCREEN)
    object LanguageSelection: Screen(NavigationRouteConstants.LANGUAGE_SELECTION_SCREEN)

}