package com.example.composenewscatcher.data.model


import androidx.room.ColumnInfo

data class NewsResponse(
    @ColumnInfo("articles")
    val articles: List<Article?>?,
    @ColumnInfo("page")
    val page: Int?,
    @ColumnInfo("page_size")
    val pageSize: Int?,
    @ColumnInfo("status")
    val status: String?,
    @ColumnInfo("total_pages")
    val totalPages: Int?
)