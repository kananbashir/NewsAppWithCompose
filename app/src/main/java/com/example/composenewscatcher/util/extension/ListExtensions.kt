package com.example.composenewscatcher.util.extension

import com.example.composenewscatcher.data.model.Article

fun List<Article>.excludeSavedArticles(): List<Article> {
    val filteredList = this.filter { !it.isSaved }
    return filteredList.ifEmpty { this }
}