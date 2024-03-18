package com.example.composenewscatcher.ui.screen.language

import android.content.Context

sealed interface LanguageScreenEvent {
    data class UpdateAppLanguage(val language: String, val context: Context): LanguageScreenEvent
}