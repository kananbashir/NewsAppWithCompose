package com.example.composenewscatcher.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ("articles_table")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val articleId: Int? = null,

    @ColumnInfo("author")
    val author: String?,

    @ColumnInfo("country")
    val country: String?,

    @ColumnInfo("excerpt")
    val excerpt: String?,

    @ColumnInfo("_id")
    val id: String?,

    @ColumnInfo("language")
    val language: String?,

    @ColumnInfo("link")
    val link: String?,

    @ColumnInfo("media")
    val media: String?,

    @ColumnInfo("published_date")
    val publishedDate: String?,

    @ColumnInfo("rank")
    val rank: Int?,

    @ColumnInfo("summary")
    val summary: String?,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("topic")
    val topic: String?,

    var isSaved: Boolean = false,

    var isCached: Boolean = false,

    var cachedTime: String? = null
)